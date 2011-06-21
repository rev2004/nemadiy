var data = [
[{o: 0.049, f: 23.475, l: "A", a: 0},
{o: 23.475, f: 47.972, l: "B", a: 1},
{o: 47.972, f: 75.474, l: "C", a: 0},
{o: 75.474, f: 87.377, l: "D", a: 1},
{o: 87.377, f: 112.225, l: "E", a: 0},
{o: 112.225, f: 138.945, l: "F", a: 1},
{o: 138.945, f: 175.496, l: "D'", a: 0},
{o: 175.496, f: 201.409, l: "B", a: 1},
{o: 201.409, f: 239.14, l: "C", a: 0},
{o: 239.14, f: 266.501, l: "A", a: 1}],
[{o: 0.012, f: 20.768, l: "A", a: 0},
{o: 20.768, f: 34.6, l: "A", a: 1},
{o: 34.6, f: 55.144, l: "A", a: 0},
{o: 55.144, f: 74.212, l: "A", a: 1},
{o: 74.212, f: 85.884, l: "A", a: 0},
{o: 85.884, f: 100.812, l: "A", a: 1},
{o: 100.812, f: 118.484, l: "A", a: 0},
{o: 118.484, f: 137.928, l: "A", a: 1},
{o: 137.928, f: 151.292, l: "D", a: 0},
{o: 151.292, f: 174.728, l: "D", a: 1},
{o: 174.728, f: 199.204, l: "A", a: 0},
{o: 199.204, f: 223.98, l: "A", a: 1},
{o: 223.98, f: 238.316, l: "A", a: 0},
{o: 238.316, f: 259.704, l: "A", a: 1}],
[{o: 0.012, f: 20.768, l: "A", a: 0},
{o: 20.768, f: 34.6, l: "E", a: 1},
{o: 34.6, f: 55.144, l: "C", a: 0},
{o: 55.144, f: 74.212, l: "C", a: 1},
{o: 74.212, f: 85.884, l: "B", a: 0},
{o: 85.884, f: 100.812, l: "C", a: 1},
{o: 100.812, f: 118.484, l: "C", a: 0},
{o: 118.484, f: 137.928, l: "C", a: 1},
{o: 137.928, f: 151.292, l: "D", a: 0},
{o: 151.292, f: 174.728, l: "D", a: 1},
{o: 174.728, f: 199.204, l: "C", a: 0},
{o: 199.204, f: 223.98, l: "F", a: 1},
{o: 223.98, f: 238.316, l: "G", a: 0},
{o: 238.316, f: 259.704, l: "B", a: 1}],
[{o: 0.547, f: 7.76, l: "3", a: 0},
{o: 7.76, f: 15.893, l: "5", a: 1},
{o: 15.893, f: 22.6, l: "6", a: 0},
{o: 22.6, f: 32.333, l: "2", a: 1},
{o: 32.333, f: 39.413, l: "1", a: 0},
{o: 39.413, f: 52.2, l: "2", a: 1},
{o: 52.2, f: 70.667, l: "1", a: 0},
{o: 70.667, f: 77.653, l: "4", a: 1},
{o: 77.653, f: 87.76, l: "3", a: 0},
{o: 87.76, f: 113.907, l: "2", a: 1},
{o: 113.907, f: 120.693, l: "1", a: 0},
{o: 120.693, f: 126.853, l: "2", a: 1},
{o: 126.853, f: 139.96, l: "1", a: 0},
{o: 139.96, f: 152.56, l: "2", a: 1},
{o: 152.56, f: 167.92, l: "5", a: 0},
{o: 167.92, f: 174.68, l: "6", a: 1},
{o: 174.68, f: 235.053, l: "1", a: 0},
{o: 235.053, f: 241.6, l: "4", a: 1},
{o: 241.6, f: 247.973, l: "3", a: 0},
{o: 247.973, f: 255.68, l: "4", a: 1},
{o: 255.68, f: 261.333, l: "7", a: 0},
{o: 261.333, f: 266.36, l: "8", a: 1}],
[{o: 0, f: 43.21, l: "a", a: 0},
{o: 43.21, f: 75.245, l: "b", a: 1},
{o: 75.245, f: 109.515, l: "c", a: 0},
{o: 109.515, f: 139.315, l: "b", a: 1},
{o: 139.315, f: 156.45, l: "d", a: 0},
{o: 156.45, f: 196.68, l: "a", a: 1},
{o: 196.68, f: 232.44, l: "b", a: 0},
{o: 232.44, f: 264.475, l: "e", a: 1},
{o: 264.475, f: 265.965, l: "f", a: 0}],
[{o: 0, f: 16.37, l: "n1", a: 0},
{o: 16.37, f: 27.121, l: "A", a: 1},
{o: 27.121, f: 87.69, l: "n2", a: 0},
{o: 87.69, f: 99.904, l: "B", a: 1},
{o: 99.904, f: 112.21, l: "B", a: 0},
{o: 112.21, f: 122.009, l: "n4", a: 1},
{o: 122.009, f: 132.342, l: "A", a: 0},
{o: 132.342, f: 169.401, l: "n5", a: 1},
{o: 169.401, f: 179.908, l: "A", a: 0},
{o: 179.908, f: 212.114, l: "n6", a: 1},
{o: 212.114, f: 222.273, l: "A", a: 0},
{o: 222.273, f: 266.426, l: "n7", a: 1}],
[{o: 0, f: 0.012, l: "G", a: 0},
{o: 0.012, f: 7.552, l: "B", a: 1},
{o: 7.552, f: 70.332, l: "F", a: 0},
{o: 70.332, f: 88.24, l: "C", a: 1},
{o: 88.24, f: 171.312, l: "E", a: 0},
{o: 171.312, f: 255.676, l: "C", a: 1},
{o: 255.676, f: 259.704, l: "D", a: 0},
{o: 259.704, f: 266.467, l: "G", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000353.ogg";

var artist = "RWC MDB C 2001 M06";

var track = "13";
