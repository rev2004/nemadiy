var data = [
[{o: 0.186, f: 7.432, l: "Z", a: 0},
{o: 7.432, f: 22.711, l: "A", a: 1},
{o: 22.711, f: 52.99, l: "B", a: 0},
{o: 52.99, f: 64.275, l: "C", a: 1},
{o: 64.275, f: 87.03, l: "B", a: 0},
{o: 87.03, f: 98.362, l: "C", a: 1},
{o: 98.362, f: 106.024, l: "D", a: 0},
{o: 106.024, f: 121.117, l: "B", a: 1},
{o: 121.117, f: 136.257, l: "C", a: 0},
{o: 136.257, f: 181.164, l: "B", a: 1},
{o: 181.164, f: 204.434, l: "C", a: 0}],
[{o: 0.188, f: 7.044, l: "A", a: 0},
{o: 7.044, f: 22.216, l: "D", a: 1},
{o: 22.216, f: 30.752, l: "D", a: 0},
{o: 30.752, f: 40.704, l: "C", a: 1},
{o: 40.704, f: 53.952, l: "C", a: 0},
{o: 53.952, f: 65.3, l: "D", a: 1},
{o: 65.3, f: 73.344, l: "C", a: 0},
{o: 73.344, f: 80.432, l: "C", a: 1},
{o: 80.432, f: 101.708, l: "C", a: 0},
{o: 101.708, f: 119.256, l: "C", a: 1},
{o: 119.256, f: 133.432, l: "C", a: 0},
{o: 133.432, f: 144.72, l: "C", a: 1},
{o: 144.72, f: 153.648, l: "C", a: 0},
{o: 153.648, f: 165.804, l: "C", a: 1},
{o: 165.804, f: 174.648, l: "C", a: 0},
{o: 174.648, f: 186.82, l: "C", a: 1},
{o: 186.82, f: 194.22, l: "D", a: 0},
{o: 194.22, f: 204.284, l: "D", a: 1}],
[{o: 0.188, f: 7.044, l: "F", a: 0},
{o: 7.044, f: 22.216, l: "D", a: 1},
{o: 22.216, f: 30.752, l: "D", a: 0},
{o: 30.752, f: 40.704, l: "C", a: 1},
{o: 40.704, f: 53.952, l: "C", a: 0},
{o: 53.952, f: 65.3, l: "G", a: 1},
{o: 65.3, f: 73.344, l: "E", a: 0},
{o: 73.344, f: 80.432, l: "E", a: 1},
{o: 80.432, f: 101.708, l: "C", a: 0},
{o: 101.708, f: 119.256, l: "C", a: 1},
{o: 119.256, f: 133.432, l: "C", a: 0},
{o: 133.432, f: 144.72, l: "A", a: 1},
{o: 144.72, f: 153.648, l: "A", a: 0},
{o: 153.648, f: 165.804, l: "C", a: 1},
{o: 165.804, f: 174.648, l: "B", a: 0},
{o: 174.648, f: 186.82, l: "B", a: 1},
{o: 186.82, f: 194.22, l: "H", a: 0},
{o: 194.22, f: 204.284, l: "I", a: 1}],
[{o: 0.627, f: 7.373, l: "5", a: 0},
{o: 7.373, f: 22.973, l: "2", a: 1},
{o: 22.973, f: 32.413, l: "1", a: 0},
{o: 32.413, f: 37.64, l: "8", a: 1},
{o: 37.64, f: 48.013, l: "1", a: 0},
{o: 48.013, f: 64.6, l: "3", a: 1},
{o: 64.6, f: 81.627, l: "1", a: 0},
{o: 81.627, f: 99.587, l: "2", a: 1},
{o: 99.587, f: 105.8, l: "6", a: 0},
{o: 105.8, f: 116.2, l: "1", a: 1},
{o: 116.2, f: 132.733, l: "2", a: 0},
{o: 132.733, f: 145.88, l: "1", a: 1},
{o: 145.88, f: 150.6, l: "3", a: 0},
{o: 150.6, f: 176.267, l: "1", a: 1},
{o: 176.267, f: 191.16, l: "2", a: 0},
{o: 191.16, f: 196.4, l: "7", a: 1},
{o: 196.4, f: 204.227, l: "4", a: 0}],
[{o: 0, f: 14.9, l: "a", a: 0},
{o: 14.9, f: 37.995, l: "b", a: 1},
{o: 37.995, f: 92.38, l: "c", a: 0},
{o: 92.38, f: 105.79, l: "d", a: 1},
{o: 105.79, f: 157.195, l: "c", a: 0},
{o: 157.195, f: 182.525, l: "b", a: 1},
{o: 182.525, f: 204.13, l: "e", a: 0}],
[{o: 0, f: 9.021, l: "n1", a: 0},
{o: 9.021, f: 24.16, l: "A", a: 1},
{o: 24.16, f: 39.323, l: "A", a: 0},
{o: 39.323, f: 52.547, l: "A", a: 1},
{o: 52.547, f: 65.805, l: "B", a: 0},
{o: 65.805, f: 73.375, l: "n4", a: 1},
{o: 73.375, f: 86.622, l: "A", a: 0},
{o: 86.622, f: 99.823, l: "B", a: 1},
{o: 99.823, f: 107.485, l: "n5", a: 0},
{o: 107.485, f: 120.709, l: "A", a: 1},
{o: 120.709, f: 137.683, l: "B", a: 0},
{o: 137.683, f: 152.741, l: "A", a: 1},
{o: 152.741, f: 167.706, l: "A", a: 0},
{o: 167.706, f: 180.756, l: "A", a: 1},
{o: 180.756, f: 204.382, l: "n9", a: 0}],
[{o: 0, f: 0.04, l: "G", a: 0},
{o: 0.04, f: 14.312, l: "F", a: 1},
{o: 14.312, f: 31.7, l: "E", a: 0},
{o: 31.7, f: 54.192, l: "C", a: 1},
{o: 54.192, f: 189.484, l: "E", a: 0},
{o: 189.484, f: 204.056, l: "D", a: 1},
{o: 204.056, f: 204.308, l: "C", a: 0},
{o: 204.308, f: 204.347, l: "G", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001081.ogg";

var artist = "Smashing Pumpkins";

var track = "Perfect";
