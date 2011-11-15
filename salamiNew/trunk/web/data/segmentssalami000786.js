var data = [
[{o: 0.098, f: 38.264, l: "A", a: 0},
{o: 38.264, f: 74.114, l: "B", a: 1},
{o: 74.114, f: 107.944, l: "A", a: 0},
{o: 107.944, f: 143.314, l: "B", a: 1},
{o: 143.314, f: 209.517, l: "A", a: 0}],
[{o: 0.072, f: 10.204, l: "G", a: 0},
{o: 10.204, f: 18.312, l: "A", a: 1},
{o: 18.312, f: 35.732, l: "G", a: 0},
{o: 35.732, f: 40.388, l: "G", a: 1},
{o: 40.388, f: 53.352, l: "G", a: 0},
{o: 53.352, f: 65.68, l: "G", a: 1},
{o: 65.68, f: 77.38, l: "G", a: 0},
{o: 77.38, f: 89.78, l: "G", a: 1},
{o: 89.78, f: 99.74, l: "G", a: 0},
{o: 99.74, f: 113.88, l: "G", a: 1},
{o: 113.88, f: 129.156, l: "G", a: 0},
{o: 129.156, f: 139.468, l: "G", a: 1},
{o: 139.468, f: 147.488, l: "G", a: 0},
{o: 147.488, f: 158.788, l: "G", a: 1},
{o: 158.788, f: 167.184, l: "G", a: 0},
{o: 167.184, f: 179.208, l: "G", a: 1},
{o: 179.208, f: 186.356, l: "G", a: 0},
{o: 186.356, f: 195.072, l: "G", a: 1},
{o: 195.072, f: 209.384, l: "G", a: 0}],
[{o: 0.072, f: 10.204, l: "G", a: 0},
{o: 10.204, f: 18.312, l: "C", a: 1},
{o: 18.312, f: 35.732, l: "G", a: 0},
{o: 35.732, f: 40.388, l: "H", a: 1},
{o: 40.388, f: 53.352, l: "E", a: 0},
{o: 53.352, f: 65.68, l: "A", a: 1},
{o: 65.68, f: 77.38, l: "E", a: 0},
{o: 77.38, f: 89.78, l: "I", a: 1},
{o: 89.78, f: 99.74, l: "B", a: 0},
{o: 99.74, f: 113.88, l: "E", a: 1},
{o: 113.88, f: 129.156, l: "A", a: 0},
{o: 129.156, f: 139.468, l: "E", a: 1},
{o: 139.468, f: 147.488, l: "E", a: 0},
{o: 147.488, f: 158.788, l: "E", a: 1},
{o: 158.788, f: 167.184, l: "B", a: 0},
{o: 167.184, f: 179.208, l: "F", a: 1},
{o: 179.208, f: 186.356, l: "F", a: 0},
{o: 186.356, f: 195.072, l: "D", a: 1},
{o: 195.072, f: 209.384, l: "D", a: 0}],
[{o: 0.653, f: 10.253, l: "1", a: 0},
{o: 10.253, f: 19.76, l: "7", a: 1},
{o: 19.76, f: 37.107, l: "5", a: 0},
{o: 37.107, f: 45.747, l: "3", a: 1},
{o: 45.747, f: 52.68, l: "8", a: 0},
{o: 52.68, f: 64.04, l: "1", a: 1},
{o: 64.04, f: 72.773, l: "4", a: 0},
{o: 72.773, f: 104, l: "2", a: 1},
{o: 104, f: 121.48, l: "3", a: 0},
{o: 121.48, f: 133.24, l: "1", a: 1},
{o: 133.24, f: 141.4, l: "4", a: 0},
{o: 141.4, f: 173.347, l: "2", a: 1},
{o: 173.347, f: 185.88, l: "3", a: 0},
{o: 185.88, f: 198.893, l: "1", a: 1},
{o: 198.893, f: 209.4, l: "6", a: 0}],
[{o: 0, f: 15.645, l: "a", a: 0},
{o: 15.645, f: 54.385, l: "b", a: 1},
{o: 54.385, f: 87.165, l: "c", a: 0},
{o: 87.165, f: 125.16, l: "b", a: 1},
{o: 125.16, f: 155.705, l: "c", a: 0},
{o: 155.705, f: 189.975, l: "b", a: 1},
{o: 189.975, f: 208.6, l: "d", a: 0}],
[{o: 0, f: 16.463, l: "n1", a: 0},
{o: 16.463, f: 46.556, l: "A", a: 1},
{o: 46.556, f: 82.466, l: "B", a: 0},
{o: 82.466, f: 88.538, l: "n2", a: 1},
{o: 88.538, f: 116.065, l: "A", a: 0},
{o: 116.065, f: 152.822, l: "B", a: 1},
{o: 152.822, f: 156.781, l: "n3", a: 0},
{o: 156.781, f: 184.483, l: "A", a: 1},
{o: 184.483, f: 209.212, l: "n4", a: 0}],
[{o: 0, f: 0.072, l: "I", a: 0},
{o: 0.072, f: 16.164, l: "H", a: 1},
{o: 16.164, f: 53.896, l: "C", a: 0},
{o: 53.896, f: 70.848, l: "A", a: 1},
{o: 70.848, f: 80.052, l: "G", a: 0},
{o: 80.052, f: 123.284, l: "C", a: 1},
{o: 123.284, f: 147.056, l: "A", a: 0},
{o: 147.056, f: 184.776, l: "C", a: 1},
{o: 184.776, f: 196.54, l: "G", a: 0},
{o: 196.54, f: 207.356, l: "E", a: 1},
{o: 207.356, f: 209.436, l: "G", a: 0},
{o: 209.436, f: 209.503, l: "I", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000786.ogg";

var artist = "Compilations";

var track = "Tear in my Beer";
