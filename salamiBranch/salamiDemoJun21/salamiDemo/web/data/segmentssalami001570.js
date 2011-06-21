var data = [
[{o: 0, f: 25.217, l: "A", a: 0},
{o: 25.217, f: 49.126, l: "A", a: 1},
{o: 49.126, f: 76.271, l: "B", a: 0},
{o: 76.271, f: 100.343, l: "A", a: 1},
{o: 100.343, f: 126.977, l: "B", a: 0},
{o: 126.977, f: 152.625, l: "A", a: 1},
{o: 152.625, f: 242.962, l: "C", a: 0}],
[{o: 0.004, f: 24.164, l: "A", a: 0},
{o: 24.164, f: 37.02, l: "A", a: 1},
{o: 37.02, f: 50.532, l: "A", a: 0},
{o: 50.532, f: 61.648, l: "A", a: 1},
{o: 61.648, f: 71.692, l: "A", a: 0},
{o: 71.692, f: 96.22, l: "A", a: 1},
{o: 96.22, f: 105.724, l: "A", a: 0},
{o: 105.724, f: 122.992, l: "A", a: 1},
{o: 122.992, f: 146.348, l: "A", a: 0},
{o: 146.348, f: 155.36, l: "A", a: 1},
{o: 155.36, f: 164.248, l: "B", a: 0},
{o: 164.248, f: 181.208, l: "A", a: 1},
{o: 181.208, f: 194.62, l: "A", a: 0},
{o: 194.62, f: 203.684, l: "C", a: 1},
{o: 203.684, f: 213.608, l: "B", a: 0},
{o: 213.608, f: 229.244, l: "A", a: 1}],
[{o: 0.004, f: 24.164, l: "A", a: 0},
{o: 24.164, f: 37.02, l: "A", a: 1},
{o: 37.02, f: 50.532, l: "A", a: 0},
{o: 50.532, f: 61.648, l: "C", a: 1},
{o: 61.648, f: 71.692, l: "A", a: 0},
{o: 71.692, f: 96.22, l: "B", a: 1},
{o: 96.22, f: 105.724, l: "A", a: 0},
{o: 105.724, f: 122.992, l: "A", a: 1},
{o: 122.992, f: 146.348, l: "B", a: 0},
{o: 146.348, f: 155.36, l: "A", a: 1},
{o: 155.36, f: 164.248, l: "D", a: 0},
{o: 164.248, f: 181.208, l: "A", a: 1},
{o: 181.208, f: 194.62, l: "E", a: 0},
{o: 194.62, f: 203.684, l: "F", a: 1},
{o: 203.684, f: 213.608, l: "G", a: 0},
{o: 213.608, f: 229.244, l: "H", a: 1}],
[{o: 0.627, f: 6.973, l: "2", a: 0},
{o: 6.973, f: 23.44, l: "1", a: 1},
{o: 23.44, f: 31.307, l: "2", a: 0},
{o: 31.307, f: 40.187, l: "4", a: 1},
{o: 40.187, f: 49.827, l: "1", a: 0},
{o: 49.827, f: 61.947, l: "3", a: 1},
{o: 61.947, f: 70.96, l: "1", a: 0},
{o: 70.96, f: 82.8, l: "2", a: 1},
{o: 82.8, f: 91.533, l: "4", a: 0},
{o: 91.533, f: 100.653, l: "1", a: 1},
{o: 100.653, f: 112.373, l: "3", a: 0},
{o: 112.373, f: 121.933, l: "1", a: 1},
{o: 121.933, f: 134.507, l: "2", a: 0},
{o: 134.507, f: 142.373, l: "4", a: 1},
{o: 142.373, f: 148.907, l: "1", a: 0},
{o: 148.907, f: 163.067, l: "2", a: 1},
{o: 163.067, f: 179.76, l: "3", a: 0},
{o: 179.76, f: 204.947, l: "1", a: 1},
{o: 204.947, f: 211.52, l: "7", a: 0},
{o: 211.52, f: 224.2, l: "6", a: 1},
{o: 224.2, f: 237.92, l: "5", a: 0},
{o: 237.92, f: 242.787, l: "8", a: 1}],
[{o: 0, f: 31.29, l: "a", a: 0},
{o: 31.29, f: 82.695, l: "b", a: 1},
{o: 82.695, f: 134.1, l: "b", a: 0},
{o: 134.1, f: 159.43, l: "a", a: 1},
{o: 159.43, f: 240.635, l: "c", a: 0},
{o: 240.635, f: 242.125, l: "d", a: 1}],
[{o: 0, f: 8.545, l: "n1", a: 0},
{o: 8.545, f: 26.053, l: "A", a: 1},
{o: 26.053, f: 32.972, l: "n2", a: 0},
{o: 32.972, f: 50.585, l: "A", a: 1},
{o: 50.585, f: 83.627, l: "B", a: 0},
{o: 83.627, f: 102.864, l: "A", a: 1},
{o: 102.864, f: 135.442, l: "B", a: 0},
{o: 135.442, f: 152.044, l: "A", a: 1},
{o: 152.044, f: 217.699, l: "n5", a: 0},
{o: 217.699, f: 228.508, l: "C", a: 1},
{o: 228.508, f: 236.797, l: "C", a: 0},
{o: 236.797, f: 242.649, l: "n7", a: 1}],
[{o: 0, f: 0.004, l: "G", a: 0},
{o: 0.004, f: 60.26, l: "C", a: 1},
{o: 60.26, f: 76.392, l: "D", a: 0},
{o: 76.392, f: 111.244, l: "C", a: 1},
{o: 111.244, f: 127.58, l: "D", a: 0},
{o: 127.58, f: 163.468, l: "C", a: 1},
{o: 163.468, f: 183.452, l: "E", a: 0},
{o: 183.452, f: 198.256, l: "F", a: 1},
{o: 198.256, f: 229.12, l: "A", a: 0},
{o: 229.12, f: 242.907, l: "G", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001570.ogg";

var artist = "RWC MDB C 2001 M05";

var track = "5";
