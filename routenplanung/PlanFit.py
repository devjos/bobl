from DataLoader import getData
import numpy as np
import datetime
from ClusterTools import computeWeightMatrix, connectBuslines
from ClusterTools import BusLineClustering,BusStopClustering,TimeClustering
from datetime import datetime
from OSM import routeToGpx
#from TSM import perform_tsm
import os
import json

def runAlgorithm(day,c_time,c_stop,c_lines):
    # load data
    my_data_indices = range(len(getData()))
    my_data_indices = [i for i in my_data_indices if getData()[i][2].weekday() == day]
    # cluster by time
    t_c = TimeClustering(indices=my_data_indices,n_clusters=c_time)
    t_c.run_clustering()
    #print(list(map(lambda x: datetime.fromtimestamp(x).strftime("%Y-%m-%d %H:%M"), t_c.getCenters())))

    output_files_json = []


    # now for every time cluster -> find bus stops
    for t_i,t in enumerate(t_c.getClasses()):
        # t = [indices to getData()]
        bs_c = BusStopClustering(t,n_clusters=c_stop)
        bs_c.run_clustering()

        bs_ctrs = bs_c.getCenters()
        matrix = computeWeightMatrix(bs_ctrs)

        # now cluster bus stops into 2 lines (Expectation Optimization)
        bl_c = BusLineClustering(stops=bs_ctrs,n_clusters=c_lines)
        bl_c.run_clustering()

        # compute connectivity stops
        connectivities = connectBuslines(bl_c.getClasses(),matrix)
        separated_lines = bl_c.getClasses()
        # find bus line and add stop
        for con in connectivities:
            mod_class,add_edge = con
            nearest_node,connectivity_node = add_edge
            separated_lines[mod_class] += [connectivity_node]

        final_routes = [[] for _ in range(len(separated_lines))]

        output_files_gpx = []

        for bl_i_i,bl_i in enumerate(bl_c.getClasses()):
            submatrix = matrix[np.ix_(bl_i,bl_i)]
            route = [int(i_stop) for i_stop in perform_tsm(bl_i,submatrix).split(" -> ")]

            final_route = [bs_ctrs[n] for n in route]
            final_routes.append(final_route)

            gpx_path = "day-{}-ntime-{}-nstop-{}-nlines-{}-time-{}-bl-{}.gpx".format(day,c_time,c_stop,c_lines,t_i,bl_i_i)
            output_files_gpx.append(gpx_path)
            with open(os.path.join("output",gpx_path),"w") as f:
                f.write(routeToGpx(final_route))

        # write bus stops information
        timefile = "day-{}-ntime-{}-nstop-{}-nlines-{}-time-{}.json".format(day,c_time,c_stop,c_lines,t_i)
        output_files_json.append(timefile)
        with open(os.path.join("output",timefile),"w") as f:
            output_str = json.dumps({"routes":output_files_gpx,"stops":[[x[1],x[0]] for x in bs_ctrs]})
            f.write(output_str)
    dayfile = "day-{}-ntime-{}-nstop-{}-nlines-{}-meta.json".format(day,c_time,c_stop,c_lines)
    with open(os.path.join("output",dayfile),"w") as f:
        json.dump({"timeAndPlan":list(zip([datetime.fromtimestamp(x).strftime("%H:%M") for x in t_c.getCenters()],output_files_json))},f)