from ortools.constraint_solver import pywrapcp
from ortools.constraint_solver import routing_enums_pb2


# Distance callback
class CreateDistanceCallback(object):
    """Create callback to calculate distances between points."""

    def __init__(self,matrix):
        """Array of distances between points."""
        self.matrix = matrix

    def Distance(self, from_node, to_node):
        return self.matrix[from_node][to_node]


def perform_tsm(names,matrix):
    # Cities
    city_names = names

    tsp_size = len(city_names)
    num_routes = 1  # The number of routes, which is 1 in the TSP.
    # Nodes are indexed from 0 to tsp_size - 1. The depot is the starting node of the route.
    depot = 0

    # Create routing model
    if tsp_size > 0:
        routing = pywrapcp.RoutingModel(tsp_size, num_routes, depot)
        search_parameters = pywrapcp.RoutingModel.DefaultSearchParameters()

        # Setting first solution heuristic: the
        # method for finding a first solution to the problem.
        search_parameters.first_solution_strategy = (
            routing_enums_pb2.FirstSolutionStrategy.PATH_CHEAPEST_ARC)

        # Create the distance callback, which takes two arguments (the from and to node indices)
        # and returns the distance between these nodes.

        dist_between_nodes = CreateDistanceCallback(matrix)
        dist_callback = dist_between_nodes.Distance
        routing.SetArcCostEvaluatorOfAllVehicles(dist_callback)
        # Solve, returns a solution if any.
        assignment = routing.SolveWithParameters(search_parameters)
        if assignment:
            # Solution cost.
            print("Total distance: " + str(assignment.ObjectiveValue()) + " miles\n")
            # Inspect solution.
            # Only one route here; otherwise iterate from 0 to routing.vehicles() - 1
            route_number = 0
            index = routing.Start(route_number)  # Index of the variable for the starting node.
            route = ''
            while not routing.IsEnd(index):
                # Convert variable indices to node indices in the displayed route.
                route += str(city_names[routing.IndexToNode(index)]) + ' -> '
                index = assignment.Value(routing.NextVar(index))
            route += str(city_names[routing.IndexToNode(index)])
            print("Route:\n\n" + route)
            return route
        else:
            print('No solution found.')
            return
    else:
        print('Specify an instance greater than 0.')
        return
