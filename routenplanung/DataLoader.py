from datetime import datetime

data = [
    [(48.128985, 11.575127),(48.133052, 11.583624),datetime.strptime("2017-12-01 18:30","%Y-%m-%d %H:%M"),"Fahrt1"],
    [(48.131620, 11.575985),(48.135745, 11.588345),datetime.strptime("2017-12-01 18:40","%Y-%m-%d %H:%M"),"Fahrt2"],
    [(48.131620, 11.575985),(48.141071, 11.598043),datetime.strptime("2017-12-01 18:50","%Y-%m-%d %H:%M"),"Fahrt3"],
    [(48.128985, 11.575127),(48.139582, 11.606712),datetime.strptime("2017-12-01 18:30","%Y-%m-%d %H:%M"),"Fahrt4"],
    [(48.141071, 11.598043),(48.138150, 11.614695),datetime.strptime("2017-12-01 18:20","%Y-%m-%d %H:%M"),"Fahrt5"],
    [(48.135745, 11.588345),(48.134542, 11.615896),datetime.strptime("2017-12-01 18:10","%Y-%m-%d %H:%M"),"Fahrt6"]
]

def access_bit(data, num):
    base = int(num/8)
    shift = num % 8
    return (data[base] & (1<<shift)) >> shift

def load_from_db():
    global data
    data = []
    import MySQLdb #52.30.65.64
    db = MySQLdb.connect(host="ec2-52-30-65-64.eu-west-1.compute.amazonaws.com",user="routenplanung",passwd="urDeN43pQ",db="bobl")
    cur = db.cursor()
    # bad coding style but whatever
    cur.execute("SELECT * FROM Demand")

    results = cur.fetchall()
    cur.close()
    db.close()

    time_day_of_week = [datetime.strptime("2017-07-0"+str(i)+" 0:0","%Y-%m-%d %H:%M") for i in range(3,10)]

    for entry in results:
        from_latlon = (float(entry[4]),float(entry[5]))
        to_latlon =  (float(entry[7]),float(entry[8]))

        time_d = entry[9]
        back = entry[10] is not None
        back_d = entry[10]
        weekdays = entry[11]
        weekdays_array = [access_bit(weekdays,i) for i in range(7)]

        for n_day,bit_day in enumerate(weekdays_array):
            if bit_day == 1:
                # simple case
                data.append( (from_latlon,to_latlon,time_day_of_week[n_day]+time_d,"") )
                if back:
                    # wayback
                    data.append( (to_latlon,from_latlon, time_day_of_week[n_day] + back_d, "") )


    print(data)

def getData():
    return data