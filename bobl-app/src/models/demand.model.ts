export class Demand {
  id : number;
  title : string;
  source : string;
  sourceLatitude : number;
  sourceLongitude : number;
  destination : string;
  destinationLatitude : number;
  destinationLongitude : number;
  //type : "permanent";
  outboundTime : string;
  waybackTime : string;
  weekdays : Array<boolean>;
}
