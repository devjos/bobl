#from numba import jit

from pyroutelib2.loadOsm import LoadOsm
from pyroutelib2.route import Router

def getGPXRouteFromTo(lat1, lon1, lat2, lon2):
    data = LoadOsm('car')
    node1 = data.findNode(lat1, lon1)
    node2 = data.findNode(lat2, lon2)

    if lat1 == lat2 and lon1 == lon2:
        return [str(node1),str(node1)],0

    router = Router(data)
    result, route = router.doRoute(node1, node2)

    if result != 'success':
        return [str(node1)],0

    distance_all = 0
    for i in range(len(route) - 1):
        distance_all += router.haversine(route[i], route[i + 1])

    return route, distance_all

def routeToGpx(list_nodes):
    """Format a route (as list of nodes) into a GPX file"""
    output = ''
    output = output + "<?xml version='1.0'?>\n"
    output = output + "<gpx version='1.1' creator='pyroute' xmlns='http://www.topografix.com/GPX/1/1' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd'>\n"

    for i in range(len(list_nodes)-1):
        output = output + " <trk>\n"
        output = output + "  <name>%s</name>\n" % ("Node "+str(i))
        output = output + "  <trkseg>\n"

        data = LoadOsm('car')
        router = Router(data)
        n_from = data.findNode(list_nodes[i][0],list_nodes[i][1])
        n_to = data.findNode(list_nodes[i+1][0],list_nodes[i+1][1])
        if n_from != n_to:

            result, route = router.doRoute(n_from, n_to)

            count = 0
            for i in route:
                node = data.rnodes[i]
                output = output + "   <trkpt lat='%f' lon='%f'>\n" % (
                    node[0],
                    node[1])
                output = output + "   </trkpt>\n"
                count = count + 1
        output = output + "  </trkseg>\n  </trk>\n"
    output = output + "</gpx>\n"

    return output


'''
import overpy
api = overpy.Overpass()

def getNearestWays(lon, lat, around):
    my_query = """(
      way
      (around:{},{},{})
      [highway~\"^(primary|secondary|tertiary|residential)$\"]
      [name];
    >;);out;
    """.format(int(around), float(lon), float(lat))
    res = api.query(my_query).get_ways()
    return res

'''
