var data = [
[{o: 0.087, f: 33.225, l: "A", a: 0},
{o: 33.225, f: 67.263, l: "B", a: 1},
{o: 67.263, f: 101.455, l: "B", a: 0},
{o: 101.455, f: 132.535, l: "A", a: 1},
{o: 132.535, f: 193.447, l: "C", a: 0},
{o: 193.447, f: 227.945, l: "B", a: 1},
{o: 227.945, f: 262.312, l: "B", a: 0},
{o: 262.312, f: 311.629, l: "D", a: 1}],
[{o: 0.136, f: 22.584, l: "C", a: 0},
{o: 22.584, f: 36.076, l: "C", a: 1},
{o: 36.076, f: 51.252, l: "C", a: 0},
{o: 51.252, f: 63.192, l: "C", a: 1},
{o: 63.192, f: 77.772, l: "C", a: 0},
{o: 77.772, f: 101.364, l: "A", a: 1},
{o: 101.364, f: 123.396, l: "C", a: 0},
{o: 123.396, f: 133.3, l: "A", a: 1},
{o: 133.3, f: 144.728, l: "B", a: 0},
{o: 144.728, f: 152.376, l: "B", a: 1},
{o: 152.376, f: 171.76, l: "B", a: 0},
{o: 171.76, f: 179.304, l: "D", a: 1},
{o: 179.304, f: 192.828, l: "B", a: 0},
{o: 192.828, f: 206.564, l: "E", a: 1},
{o: 206.564, f: 227.368, l: "C", a: 0},
{o: 227.368, f: 238.208, l: "F", a: 1},
{o: 238.208, f: 253.732, l: "A", a: 0},
{o: 253.732, f: 262.712, l: "C", a: 1},
{o: 262.712, f: 272.164, l: "G", a: 0},
{o: 272.164, f: 290.452, l: "H", a: 1},
{o: 290.452, f: 302.512, l: "I", a: 0},
{o: 302.512, f: 311.44, l: "J", a: 1}],
[{o: 0.136, f: 22.584, l: "C", a: 0},
{o: 22.584, f: 36.076, l: "C", a: 1},
{o: 36.076, f: 51.252, l: "B", a: 0},
{o: 51.252, f: 63.192, l: "C", a: 1},
{o: 63.192, f: 77.772, l: "B", a: 0},
{o: 77.772, f: 101.364, l: "A", a: 1},
{o: 101.364, f: 123.396, l: "B", a: 0},
{o: 123.396, f: 133.3, l: "A", a: 1},
{o: 133.3, f: 144.728, l: "D", a: 0},
{o: 144.728, f: 152.376, l: "E", a: 1},
{o: 152.376, f: 171.76, l: "F", a: 0},
{o: 171.76, f: 179.304, l: "G", a: 1},
{o: 179.304, f: 192.828, l: "H", a: 0},
{o: 192.828, f: 206.564, l: "I", a: 1},
{o: 206.564, f: 227.368, l: "C", a: 0},
{o: 227.368, f: 238.208, l: "J", a: 1},
{o: 238.208, f: 253.732, l: "A", a: 0},
{o: 253.732, f: 262.712, l: "K", a: 1},
{o: 262.712, f: 272.164, l: "L", a: 0},
{o: 272.164, f: 290.452, l: "M", a: 1},
{o: 290.452, f: 302.512, l: "N", a: 0},
{o: 302.512, f: 311.44, l: "O", a: 1}],
[{o: 0.547, f: 2.467, l: "8", a: 0},
{o: 2.467, f: 21.107, l: "3", a: 1},
{o: 21.107, f: 65.507, l: "2", a: 0},
{o: 65.507, f: 70.733, l: "5", a: 1},
{o: 70.733, f: 90.787, l: "2", a: 0},
{o: 90.787, f: 100.733, l: "1", a: 1},
{o: 100.733, f: 119.347, l: "3", a: 0},
{o: 119.347, f: 124.893, l: "2", a: 1},
{o: 124.893, f: 132.653, l: "3", a: 0},
{o: 132.653, f: 156.373, l: "1", a: 1},
{o: 156.373, f: 163.093, l: "4", a: 0},
{o: 163.093, f: 185.8, l: "1", a: 1},
{o: 185.8, f: 191.347, l: "4", a: 0},
{o: 191.347, f: 209.4, l: "2", a: 1},
{o: 209.4, f: 225.453, l: "1", a: 0},
{o: 225.453, f: 231.173, l: "5", a: 1},
{o: 231.173, f: 238.053, l: "2", a: 0},
{o: 238.053, f: 300.52, l: "1", a: 1},
{o: 300.52, f: 308.253, l: "6", a: 0},
{o: 308.253, f: 311.453, l: "7", a: 1}],
[{o: 0, f: 32.78, l: "a", a: 0},
{o: 32.78, f: 64.815, l: "b", a: 1},
{o: 64.815, f: 100.575, l: "b", a: 0},
{o: 100.575, f: 160.175, l: "c", a: 1},
{o: 160.175, f: 192.21, l: "b", a: 0},
{o: 192.21, f: 227.97, l: "b", a: 1},
{o: 227.97, f: 260.005, l: "b", a: 0},
{o: 260.005, f: 311.41, l: "b", a: 1}],
[{o: 0, f: 4.4, l: "n1", a: 0},
{o: 4.4, f: 22.616, l: "B", a: 1},
{o: 22.616, f: 37.779, l: "n2", a: 0},
{o: 37.779, f: 71.819, l: "A", a: 1},
{o: 71.819, f: 102.76, l: "A", a: 0},
{o: 102.76, f: 121.707, l: "B", a: 1},
{o: 121.707, f: 164.188, l: "n4", a: 0},
{o: 164.188, f: 198.02, l: "A", a: 1},
{o: 198.02, f: 232.687, l: "A", a: 0},
{o: 232.687, f: 263.616, l: "A", a: 1},
{o: 263.616, f: 277.351, l: "C", a: 0},
{o: 277.351, f: 284.154, l: "n7", a: 1},
{o: 284.154, f: 297.68, l: "C", a: 0},
{o: 297.68, f: 311.38, l: "n8", a: 1}],
[{o: 0, f: 0.032, l: "H", a: 0},
{o: 0.032, f: 1.26, l: "B", a: 1},
{o: 1.26, f: 29.072, l: "G", a: 0},
{o: 29.072, f: 38.176, l: "C", a: 1},
{o: 38.176, f: 100.932, l: "F", a: 0},
{o: 100.932, f: 135.396, l: "G", a: 1},
{o: 135.396, f: 154.932, l: "F", a: 0},
{o: 154.932, f: 163.092, l: "C", a: 1},
{o: 163.092, f: 190.692, l: "F", a: 0},
{o: 190.692, f: 233.716, l: "C", a: 1},
{o: 233.716, f: 300.676, l: "F", a: 0},
{o: 300.676, f: 311.508, l: "C", a: 1},
{o: 311.508, f: 311.547, l: "H", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001032.ogg";
