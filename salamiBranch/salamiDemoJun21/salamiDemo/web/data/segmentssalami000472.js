var data = [
[{o: 0, f: 10.232, l: "A", a: 0},
{o: 10.232, f: 30.599, l: "B", a: 1},
{o: 30.599, f: 51.086, l: "C", a: 0},
{o: 51.086, f: 81.764, l: "D", a: 1},
{o: 81.764, f: 89.379, l: "A", a: 0},
{o: 89.379, f: 109.778, l: "B", a: 1},
{o: 109.778, f: 130.192, l: "C", a: 0},
{o: 130.192, f: 160.854, l: "D", a: 1},
{o: 160.854, f: 181.217, l: "A", a: 0},
{o: 181.217, f: 211.913, l: "D", a: 1},
{o: 211.913, f: 243.174, l: "A", a: 0}],
[{o: 0.004, f: 18.2, l: "A", a: 0},
{o: 18.2, f: 31.288, l: "A", a: 1},
{o: 31.288, f: 41.82, l: "A", a: 0},
{o: 41.82, f: 59.688, l: "A", a: 1},
{o: 59.688, f: 76.604, l: "A", a: 0},
{o: 76.604, f: 89.368, l: "A", a: 1},
{o: 89.368, f: 99.896, l: "A", a: 0},
{o: 99.896, f: 111.072, l: "A", a: 1},
{o: 111.072, f: 121.284, l: "A", a: 0},
{o: 121.284, f: 138.836, l: "A", a: 1},
{o: 138.836, f: 158.304, l: "A", a: 0},
{o: 158.304, f: 181.284, l: "B", a: 1},
{o: 181.284, f: 204.26, l: "C", a: 0},
{o: 204.26, f: 214.472, l: "D", a: 1},
{o: 214.472, f: 224.368, l: "E", a: 0},
{o: 224.368, f: 239.696, l: "F", a: 1}],
[{o: 0.004, f: 18.2, l: "A", a: 0},
{o: 18.2, f: 31.288, l: "C", a: 1},
{o: 31.288, f: 41.82, l: "B", a: 0},
{o: 41.82, f: 59.688, l: "B", a: 1},
{o: 59.688, f: 76.604, l: "B", a: 0},
{o: 76.604, f: 89.368, l: "A", a: 1},
{o: 89.368, f: 99.896, l: "D", a: 0},
{o: 99.896, f: 111.072, l: "E", a: 1},
{o: 111.072, f: 121.284, l: "B", a: 0},
{o: 121.284, f: 138.836, l: "B", a: 1},
{o: 138.836, f: 158.304, l: "B", a: 0},
{o: 158.304, f: 181.284, l: "F", a: 1},
{o: 181.284, f: 204.26, l: "G", a: 0},
{o: 204.26, f: 214.472, l: "H", a: 1},
{o: 214.472, f: 224.368, l: "I", a: 0},
{o: 224.368, f: 239.696, l: "J", a: 1}],
[{o: 0.653, f: 9.907, l: "1", a: 0},
{o: 9.907, f: 21.4, l: "3", a: 1},
{o: 21.4, f: 32.893, l: "4", a: 0},
{o: 32.893, f: 41.827, l: "3", a: 1},
{o: 41.827, f: 50.76, l: "5", a: 0},
{o: 50.76, f: 75.667, l: "2", a: 1},
{o: 75.667, f: 87.8, l: "1", a: 0},
{o: 87.8, f: 100.547, l: "3", a: 1},
{o: 100.547, f: 112.04, l: "4", a: 0},
{o: 112.04, f: 121.613, l: "3", a: 1},
{o: 121.613, f: 129.907, l: "5", a: 0},
{o: 129.907, f: 156.707, l: "2", a: 1},
{o: 156.707, f: 180.973, l: "1", a: 0},
{o: 180.973, f: 197.56, l: "2", a: 1},
{o: 197.56, f: 209.693, l: "7", a: 0},
{o: 209.693, f: 219.267, l: "1", a: 1},
{o: 219.267, f: 237.133, l: "6", a: 0},
{o: 237.133, f: 241.693, l: "8", a: 1}],
[{o: 0, f: 37.25, l: "a", a: 0},
{o: 37.25, f: 50.66, l: "b", a: 1},
{o: 50.66, f: 79.715, l: "c", a: 0},
{o: 79.715, f: 116.965, l: "a", a: 1},
{o: 116.965, f: 130.375, l: "b", a: 0},
{o: 130.375, f: 159.43, l: "c", a: 1},
{o: 159.43, f: 177.31, l: "b", a: 0},
{o: 177.31, f: 213.07, l: "c", a: 1},
{o: 213.07, f: 242.87, l: "c", a: 0}],
[{o: 0, f: 5.155, l: "n1", a: 0},
{o: 5.155, f: 45.999, l: "B", a: 1},
{o: 45.999, f: 51.107, l: "n2", a: 0},
{o: 51.107, f: 84.3, l: "A", a: 1},
{o: 84.3, f: 125.144, l: "B", a: 0},
{o: 125.144, f: 130.252, l: "n3", a: 1},
{o: 130.252, f: 163.445, l: "A", a: 0},
{o: 163.445, f: 181.313, l: "n4", a: 1},
{o: 181.313, f: 214.506, l: "A", a: 0},
{o: 214.506, f: 224.723, l: "C", a: 1},
{o: 224.723, f: 234.928, l: "C", a: 0},
{o: 234.928, f: 243.02, l: "n5", a: 1}],
[{o: 0, f: 0.004, l: "F", a: 0},
{o: 0.004, f: 19.16, l: "C", a: 1},
{o: 19.16, f: 38.304, l: "A", a: 0},
{o: 38.304, f: 48.516, l: "D", a: 1},
{o: 48.516, f: 67.356, l: "E", a: 0},
{o: 67.356, f: 98.304, l: "C", a: 1},
{o: 98.304, f: 117.452, l: "A", a: 0},
{o: 117.452, f: 129.904, l: "D", a: 1},
{o: 129.904, f: 145.856, l: "E", a: 0},
{o: 145.856, f: 178.728, l: "C", a: 1},
{o: 178.728, f: 197.568, l: "E", a: 0},
{o: 197.568, f: 227.556, l: "C", a: 1},
{o: 227.556, f: 239.696, l: "B", a: 0},
{o: 239.696, f: 243.133, l: "F", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000472.ogg";

var artist = "RWC MDB P 2001 M07";

var track = "7";
