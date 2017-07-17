export class Demand {
  id : number;
  title : string;
  source : string;
  sourceLatitude : string;
  sourceLongitude : string;
  destination : string;
  destinationLatitude : string;
  destinationLongitude : string;
  //type : "permanent";
  outboundTime : string;
  waybackTime : string;
  weekdays : Array<number>; //[0,1] allowed
}
