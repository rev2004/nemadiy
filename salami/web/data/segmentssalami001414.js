var data = [
[{o: 0.367, f: 37.456, l: "A", a: 0},
{o: 37.456, f: 89.726, l: "A", a: 1},
{o: 89.726, f: 141.908, l: "A", a: 0},
{o: 141.908, f: 292.967, l: "A", a: 1},
{o: 292.967, f: 327.541, l: "A'", a: 0},
{o: 327.541, f: 396.498, l: "A", a: 1},
{o: 396.498, f: 401.439, l: "Z", a: 0}],
[{o: 0.228, f: 10.344, l: "F", a: 0},
{o: 10.344, f: 18.572, l: "G", a: 1},
{o: 18.572, f: 35.22, l: "D", a: 0},
{o: 35.22, f: 44.504, l: "B", a: 1},
{o: 44.504, f: 54.948, l: "B", a: 0},
{o: 54.948, f: 64.6, l: "B", a: 1},
{o: 64.6, f: 71.528, l: "B", a: 0},
{o: 71.528, f: 88.596, l: "A", a: 1},
{o: 88.596, f: 106.384, l: "B", a: 0},
{o: 106.384, f: 123.532, l: "C", a: 1},
{o: 123.532, f: 140.856, l: "A", a: 0},
{o: 140.856, f: 160.48, l: "C", a: 1},
{o: 160.48, f: 172.128, l: "C", a: 0},
{o: 172.128, f: 188.756, l: "C", a: 1},
{o: 188.756, f: 196.864, l: "C", a: 0},
{o: 196.864, f: 207.824, l: "C", a: 1},
{o: 207.824, f: 223.572, l: "D", a: 0},
{o: 223.572, f: 231.392, l: "H", a: 1},
{o: 231.392, f: 242.824, l: "I", a: 0},
{o: 242.824, f: 258.604, l: "J", a: 1},
{o: 258.604, f: 272.144, l: "K", a: 0},
{o: 272.144, f: 292.236, l: "L", a: 1},
{o: 292.236, f: 309.212, l: "E", a: 0},
{o: 309.212, f: 327.852, l: "M", a: 1},
{o: 327.852, f: 344.668, l: "B", a: 0},
{o: 344.668, f: 360.344, l: "N", a: 1},
{o: 360.344, f: 380.708, l: "O", a: 0},
{o: 380.708, f: 396.62, l: "E", a: 1}],
[{o: 0.228, f: 10.344, l: "E", a: 0},
{o: 10.344, f: 18.572, l: "F", a: 1},
{o: 18.572, f: 35.22, l: "G", a: 0},
{o: 35.22, f: 44.504, l: "B", a: 1},
{o: 44.504, f: 54.948, l: "B", a: 0},
{o: 54.948, f: 64.6, l: "B", a: 1},
{o: 64.6, f: 71.528, l: "B", a: 0},
{o: 71.528, f: 88.596, l: "A", a: 1},
{o: 88.596, f: 106.384, l: "B", a: 0},
{o: 106.384, f: 123.532, l: "C", a: 1},
{o: 123.532, f: 140.856, l: "A", a: 0},
{o: 140.856, f: 160.48, l: "C", a: 1},
{o: 160.48, f: 172.128, l: "D", a: 0},
{o: 172.128, f: 188.756, l: "D", a: 1},
{o: 188.756, f: 196.864, l: "H", a: 0},
{o: 196.864, f: 207.824, l: "I", a: 1},
{o: 207.824, f: 223.572, l: "J", a: 0},
{o: 223.572, f: 231.392, l: "K", a: 1},
{o: 231.392, f: 242.824, l: "L", a: 0},
{o: 242.824, f: 258.604, l: "M", a: 1},
{o: 258.604, f: 272.144, l: "N", a: 0},
{o: 272.144, f: 292.236, l: "O", a: 1},
{o: 292.236, f: 309.212, l: "P", a: 0},
{o: 309.212, f: 327.852, l: "Q", a: 1},
{o: 327.852, f: 344.668, l: "B", a: 0},
{o: 344.668, f: 360.344, l: "R", a: 1},
{o: 360.344, f: 380.708, l: "S", a: 0},
{o: 380.708, f: 396.62, l: "T", a: 1}],
[{o: 0.44, f: 18.347, l: "5", a: 0},
{o: 18.347, f: 34.44, l: "2", a: 1},
{o: 34.44, f: 70.76, l: "1", a: 0},
{o: 70.76, f: 86.227, l: "2", a: 1},
{o: 86.227, f: 123.453, l: "1", a: 0},
{o: 123.453, f: 139.4, l: "2", a: 1},
{o: 139.4, f: 224.173, l: "1", a: 0},
{o: 224.173, f: 228.88, l: "8", a: 1},
{o: 228.88, f: 240.227, l: "1", a: 0},
{o: 240.227, f: 257.547, l: "6", a: 1},
{o: 257.547, f: 289.48, l: "4", a: 0},
{o: 289.48, f: 325.08, l: "3", a: 1},
{o: 325.08, f: 360.227, l: "1", a: 0},
{o: 360.227, f: 376.067, l: "2", a: 1},
{o: 376.067, f: 396.04, l: "3", a: 0},
{o: 396.04, f: 401.293, l: "7", a: 1}],
[{o: 0, f: 34.27, l: "a", a: 0},
{o: 34.27, f: 111.005, l: "b", a: 1},
{o: 111.005, f: 142.295, l: "b", a: 0},
{o: 142.295, f: 210.09, l: "c", a: 1},
{o: 210.09, f: 250.32, l: "d", a: 0},
{o: 250.32, f: 292.04, l: "d", a: 1},
{o: 292.04, f: 324.82, l: "e", a: 0},
{o: 324.82, f: 401.555, l: "b", a: 1}],
[{o: 0, f: 11.041, l: "n1", a: 0},
{o: 11.041, f: 23.859, l: "A", a: 1},
{o: 23.859, f: 71.959, l: "n2", a: 0},
{o: 71.959, f: 84.776, l: "A", a: 1},
{o: 84.776, f: 401.287, l: "n3", a: 0}],
[{o: 0, f: 0.228, l: "G", a: 0},
{o: 0.228, f: 1.472, l: "F", a: 1},
{o: 1.472, f: 36.016, l: "A", a: 0},
{o: 36.016, f: 67.272, l: "F", a: 1},
{o: 67.272, f: 88.328, l: "A", a: 0},
{o: 88.328, f: 123.268, l: "F", a: 1},
{o: 123.268, f: 224.356, l: "A", a: 0},
{o: 224.356, f: 290.932, l: "D", a: 1},
{o: 290.932, f: 326.248, l: "C", a: 0},
{o: 326.248, f: 354.124, l: "F", a: 1},
{o: 354.124, f: 392.824, l: "A", a: 0},
{o: 392.824, f: 396.62, l: "D", a: 1},
{o: 396.62, f: 401.425, l: "G", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001414.ogg";

var artist = "Soulive";

var track = "First Street";
