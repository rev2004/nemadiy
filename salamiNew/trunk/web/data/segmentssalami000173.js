var data = [
[{o: 0, f: 2.526, l: "F", a: 0},
{o: 2.526, f: 17.987, l: "A", a: 1},
{o: 17.987, f: 49.001, l: "B", a: 0},
{o: 49.001, f: 66.445, l: "C", a: 1},
{o: 66.445, f: 95.394, l: "D", a: 0},
{o: 95.394, f: 110.892, l: "A", a: 1},
{o: 110.892, f: 141.756, l: "B", a: 0},
{o: 141.756, f: 159.213, l: "C", a: 1},
{o: 159.213, f: 188.268, l: "D", a: 0},
{o: 188.268, f: 203.76, l: "E", a: 1},
{o: 203.76, f: 233.002, l: "D'", a: 0},
{o: 233.002, f: 246.156, l: "A", a: 1}],
[{o: 0.008, f: 8.244, l: "A", a: 0},
{o: 8.244, f: 17.916, l: "A", a: 1},
{o: 17.916, f: 27.596, l: "C", a: 0},
{o: 27.596, f: 44.524, l: "C", a: 1},
{o: 44.524, f: 49.368, l: "B", a: 0},
{o: 49.368, f: 65.816, l: "B", a: 1},
{o: 65.816, f: 87.6, l: "B", a: 0},
{o: 87.6, f: 98.72, l: "A", a: 1},
{o: 98.72, f: 110.82, l: "A", a: 0},
{o: 110.82, f: 120.008, l: "C", a: 1},
{o: 120.008, f: 136.948, l: "D", a: 0},
{o: 136.948, f: 146.624, l: "B", a: 1},
{o: 146.624, f: 158.24, l: "B", a: 0},
{o: 158.24, f: 172.268, l: "B", a: 1},
{o: 172.268, f: 180.5, l: "B", a: 0},
{o: 180.5, f: 188.24, l: "A", a: 1},
{o: 188.24, f: 203.476, l: "A", a: 0},
{o: 203.476, f: 210.736, l: "E", a: 1},
{o: 210.736, f: 221.38, l: "B", a: 0},
{o: 221.38, f: 231.548, l: "F", a: 1},
{o: 231.548, f: 242.196, l: "G", a: 0}],
[{o: 0.008, f: 8.244, l: "A", a: 0},
{o: 8.244, f: 17.916, l: "A", a: 1},
{o: 17.916, f: 27.596, l: "E", a: 0},
{o: 27.596, f: 44.524, l: "C", a: 1},
{o: 44.524, f: 49.368, l: "F", a: 0},
{o: 49.368, f: 65.816, l: "B", a: 1},
{o: 65.816, f: 87.6, l: "B", a: 0},
{o: 87.6, f: 98.72, l: "D", a: 1},
{o: 98.72, f: 110.82, l: "A", a: 0},
{o: 110.82, f: 120.008, l: "C", a: 1},
{o: 120.008, f: 136.948, l: "G", a: 0},
{o: 136.948, f: 146.624, l: "H", a: 1},
{o: 146.624, f: 158.24, l: "I", a: 0},
{o: 158.24, f: 172.268, l: "B", a: 1},
{o: 172.268, f: 180.5, l: "B", a: 0},
{o: 180.5, f: 188.24, l: "D", a: 1},
{o: 188.24, f: 203.476, l: "J", a: 0},
{o: 203.476, f: 210.736, l: "K", a: 1},
{o: 210.736, f: 221.38, l: "B", a: 0},
{o: 221.38, f: 231.548, l: "L", a: 1},
{o: 231.548, f: 242.196, l: "M", a: 0}],
[{o: 0.493, f: 17.667, l: "4", a: 0},
{o: 17.667, f: 53.48, l: "2", a: 1},
{o: 53.48, f: 60.253, l: "1", a: 0},
{o: 60.253, f: 67.027, l: "5", a: 1},
{o: 67.027, f: 84.44, l: "1", a: 0},
{o: 84.44, f: 91.213, l: "3", a: 1},
{o: 91.213, f: 96.067, l: "8", a: 0},
{o: 96.067, f: 110.573, l: "4", a: 1},
{o: 110.573, f: 153.16, l: "2", a: 0},
{o: 153.16, f: 158.013, l: "5", a: 1},
{o: 158.013, f: 177.347, l: "1", a: 0},
{o: 177.347, f: 202.027, l: "3", a: 1},
{o: 202.027, f: 211.213, l: "6", a: 0},
{o: 211.213, f: 226.227, l: "1", a: 1},
{o: 226.227, f: 233, l: "3", a: 0},
{o: 233, f: 239.267, l: "4", a: 1},
{o: 239.267, f: 245.987, l: "7", a: 0}],
[{o: 0, f: 58.855, l: "a", a: 0},
{o: 58.855, f: 95.36, l: "b", a: 1},
{o: 95.36, f: 151.98, l: "a", a: 0},
{o: 151.98, f: 188.485, l: "b", a: 1},
{o: 188.485, f: 210.835, l: "c", a: 0},
{o: 210.835, f: 245.85, l: "b", a: 1}],
[{o: 0, f: 2.461, l: "n1", a: 0},
{o: 2.461, f: 68.278, l: "A", a: 1},
{o: 68.278, f: 95.364, l: "B", a: 0},
{o: 95.364, f: 161.181, l: "A", a: 1},
{o: 161.181, f: 188.267, l: "B", a: 0},
{o: 188.267, f: 205.682, l: "n4", a: 1},
{o: 205.682, f: 232.78, l: "B", a: 0},
{o: 232.78, f: 245.992, l: "n5", a: 1}],
[{o: 0, f: 0.008, l: "E", a: 0},
{o: 0.008, f: 4.376, l: "C", a: 1},
{o: 4.376, f: 49.368, l: "D", a: 0},
{o: 49.368, f: 67.276, l: "B", a: 1},
{o: 67.276, f: 97.28, l: "A", a: 0},
{o: 97.28, f: 144.204, l: "D", a: 1},
{o: 144.204, f: 160.176, l: "B", a: 0},
{o: 160.176, f: 190.176, l: "A", a: 1},
{o: 190.176, f: 211.464, l: "D", a: 0},
{o: 211.464, f: 220.18, l: "B", a: 1},
{o: 220.18, f: 231.06, l: "A", a: 0},
{o: 231.06, f: 242, l: "C", a: 1},
{o: 242, f: 246.08, l: "E", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000173.ogg";

var artist = "RWC MDB P 2001 M03";

var track = "12";
