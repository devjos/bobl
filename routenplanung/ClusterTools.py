from numba import jit
from sklearn.cluster import KMeans
import numpy as np
import utm
from sklearn.preprocessing import normalize,scale
from DataLoader import getData
from sklearn.mixture import GaussianMixture

from OSM import getGPXRouteFromTo

global squarechar

def convertLatLonToUTMCoords(lat,lon):
    x,y,square,char = utm.from_latlon(lat,lon)
    global squarechar
    squarechar = [square,char]
    return [x,y]

def convertUTMCoordsToLatLon(la,lo):
    global squarechar
    lat,lon = utm.to_latlon(la,lo,squarechar[0],squarechar[1])
    return [lat,lon]

def normalizeXYVectors(np_vectors):
    return normalize(X=np_vectors)

class TimeClustering(object):
    def __init__(self,indices,n_clusters):
        self.indices = indices
        self.n_clusters = n_clusters
        self.model = None


    def run_clustering(self):
        datapoints = [[getData()[i][2].timestamp()] for i in self.indices]
        n_splits = min(self.n_clusters,len(self.indices))
        self.model = KMeans(n_clusters=n_splits, random_state=0).fit(datapoints)

        self.classes = [[] for _ in range(self.n_clusters)]
        for i,l in enumerate(self.model.labels_):
            self.classes[l] += [self.indices[i]]

        return self.classes

    def getClasses(self):
        if self.classes is None:
            self.run_clustering()
        return self.classes

    def getCenters(self):
        if self.model is None:
            self.run_clustering()
        return self.model.cluster_centers_

class BusStopClustering(object):
    def __init__(self,indices,n_clusters):
        self.indices = indices
        self.n_clusters = n_clusters
        self.model = None

    def run_clustering(self):
        conv_short = lambda x:convertLatLonToUTMCoords(x[0],x[1])
        datapoints_from = [conv_short(getData()[i][0]) for i in self.indices]
        datapoints_to = [conv_short(getData()[i][1]) for i in self.indices]
        datapoints = datapoints_from + datapoints_to

        n_splits = min(self.n_clusters,len(self.indices))

        self.model = KMeans(n_clusters=n_splits, random_state=0).fit(datapoints)

        self.classes = [[] for _ in range(n_splits)]
        for i,l in enumerate(self.indices):
            self.classes[self.model.labels_[i]] += [self.indices[i]]
            self.classes[self.model.labels_[i+len(self.indices)]] += [-self.indices[i]]

    def getClasses(self):
        if self.classes is None:
            self.run_clustering()
        return self.classes

    def getCentersRaw(self):
        if self.model is None:
            self.run_clustering()
        return self.model.cluster_centers_

    def getCenters(self):
        ctrs = self.getCentersRaw()
        conv_short = lambda c: convertUTMCoordsToLatLon(c[0], c[1])
        latlon_centers = list(map(conv_short,ctrs))
        return latlon_centers

class BusLineClustering(object):
    def __init__(self,stops,n_clusters):
        self.stops = stops
        self.n_clusters = n_clusters
        self.model = None
        self.labels = None

    def run_clustering(self):
        conv_short = lambda x: convertLatLonToUTMCoords(x[0], x[1])
        datapoints = [conv_short(i) for i in self.stops]

        n_splits = min(self.n_clusters, len(self.stops))

        self.model = GaussianMixture(n_components=n_splits).fit(datapoints)

        self.classes = [[] for _ in range(n_splits)]

        self.labels = self.model.predict(datapoints)

        for i in range(len(self.stops)):
            self.classes[self.labels[i]] += [i]

    def getStops(self):
        return self.stops

    def getClasses(self):
        return self.classes

    def getCentersRaw(self):
        if self.model is None:
            self.run_clustering()
        return self.model.means_

    def getCenters(self):
        ctrs = self.getCentersRaw()
        conv_short = lambda c: convertUTMCoordsToLatLon(c[0], c[1])
        latlon_centers = list(map(conv_short,ctrs))
        return latlon_centers


def computeWeightMatrix(coordsLatLon,undirected=True):
    my_coords = []
    for c in coordsLatLon:
        lat=c[0]
        lon=c[1]
        my_coords.append((lat,lon))

    #m = [[0]*len(my_coords) for _ in range(len(my_coords))]
    m = np.zeros(len(my_coords)**2)
    m = m.reshape(-1,len(my_coords))

    # compute weights
    for row in range(m.shape[0]):
        for column in range(m.shape[1]):
            if row < column:
                if undirected:
                    m[row,column] = getGPXRouteFromTo(*(my_coords[row]),*(my_coords[column]))[1]
                    m[column,row] = m[row,column]
                else:
                    m[row,column] = getGPXRouteFromTo(*(my_coords[row]),*(my_coords[column]))[1]
                    m[column,row] = getGPXRouteFromTo(*(my_coords[column]),*(my_coords[row]))[1]

    print("Weighted matrix successfully computed!")
    return m

def connectBuslines(lines_stops,w_matrix):
    '''
    Connect each line by determine one shared bus stop
    :param lines_stops: like this: [[0, 2], [1, 3]] -> first line has stop 0 and 2, second line has ....
    :param w_matrix: simple weight matrix
    :return: return list of tuples for bus stops -> (a,b) -> include b as stop in bus line with a
    '''
    flatten = lambda l: [item for sublist in l for item in sublist]
    lines = list(range(len(lines_stops)))
    connected = [lines.pop()]
    connections = []
    while len(lines) != 0:
        next_connect = None
        next_penalty = float("inf")
        next_line = None
        for x in lines:
            x_stops = lines_stops[x]
            may_connect_to = flatten([lines_stops[t] for t in connected])

            for x_stop in x_stops:
                for mc in may_connect_to:
                    if(x_stop == mc):
                        print("ERROR! :-(")
                    penalty = w_matrix[x_stop,mc]+w_matrix[mc,x_stop]
                    if next_penalty > penalty:
                        next_penalty = penalty
                        next_connect = (x_stop,mc)
                        next_line = x

        connected.append(next_line)
        lines.remove(next_line)
        connections.append( (next_line,next_connect) )

    return connections
