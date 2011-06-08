var data = [
[{o: 0.372, f: 7.239, l: "A", a: 0},
{o: 7.239, f: 23.398, l: "B", a: 1},
{o: 23.398, f: 32.524, l: "C", a: 0},
{o: 32.524, f: 37.273, l: "A'", a: 1},
{o: 37.273, f: 53.154, l: "B", a: 0},
{o: 53.154, f: 62.356, l: "C", a: 1},
{o: 62.356, f: 64.909, l: "A''", a: 0},
{o: 64.909, f: 80.796, l: "B", a: 1},
{o: 80.796, f: 89.963, l: "C", a: 0},
{o: 89.963, f: 92.294, l: "A''", a: 1},
{o: 92.294, f: 108.308, l: "B", a: 0},
{o: 108.308, f: 117.41, l: "C", a: 1},
{o: 117.41, f: 133.495, l: "B", a: 0},
{o: 133.495, f: 146.472, l: "C", a: 1}],
[{o: 0.236, f: 5.628, l: "E", a: 0},
{o: 5.628, f: 10.176, l: "A", a: 1},
{o: 10.176, f: 17.916, l: "A", a: 0},
{o: 17.916, f: 29.98, l: "A", a: 1},
{o: 29.98, f: 37.14, l: "A", a: 0},
{o: 37.14, f: 43.72, l: "A", a: 1},
{o: 43.72, f: 50.34, l: "A", a: 0},
{o: 50.34, f: 58.088, l: "A", a: 1},
{o: 58.088, f: 63.508, l: "A", a: 0},
{o: 63.508, f: 77.424, l: "A", a: 1},
{o: 77.424, f: 87.188, l: "A", a: 0},
{o: 87.188, f: 93.488, l: "A", a: 1},
{o: 93.488, f: 102.668, l: "A", a: 0},
{o: 102.668, f: 113.184, l: "E", a: 1},
{o: 113.184, f: 119.464, l: "E", a: 0},
{o: 119.464, f: 128.944, l: "A", a: 1},
{o: 128.944, f: 137.984, l: "E", a: 0},
{o: 137.984, f: 143.788, l: "E", a: 1}],
[{o: 0.236, f: 5.628, l: "D", a: 0},
{o: 5.628, f: 10.176, l: "A", a: 1},
{o: 10.176, f: 17.916, l: "A", a: 0},
{o: 17.916, f: 29.98, l: "B", a: 1},
{o: 29.98, f: 37.14, l: "B", a: 0},
{o: 37.14, f: 43.72, l: "B", a: 1},
{o: 43.72, f: 50.34, l: "C", a: 0},
{o: 50.34, f: 58.088, l: "F", a: 1},
{o: 58.088, f: 63.508, l: "B", a: 0},
{o: 63.508, f: 77.424, l: "B", a: 1},
{o: 77.424, f: 87.188, l: "B", a: 0},
{o: 87.188, f: 93.488, l: "C", a: 1},
{o: 93.488, f: 102.668, l: "B", a: 0},
{o: 102.668, f: 113.184, l: "G", a: 1},
{o: 113.184, f: 119.464, l: "E", a: 0},
{o: 119.464, f: 128.944, l: "B", a: 1},
{o: 128.944, f: 137.984, l: "E", a: 0},
{o: 137.984, f: 143.788, l: "H", a: 1}],
[{o: 0.76, f: 7.333, l: "7", a: 0},
{o: 7.333, f: 21.093, l: "2", a: 1},
{o: 21.093, f: 37.173, l: "1", a: 0},
{o: 37.173, f: 49.227, l: "2", a: 1},
{o: 49.227, f: 65.267, l: "1", a: 0},
{o: 65.267, f: 76.92, l: "4", a: 1},
{o: 76.92, f: 91.787, l: "3", a: 0},
{o: 91.787, f: 98.68, l: "4", a: 1},
{o: 98.68, f: 108.373, l: "6", a: 0},
{o: 108.373, f: 117.547, l: "3", a: 1},
{o: 117.547, f: 124.413, l: "5", a: 0},
{o: 124.413, f: 131.253, l: "6", a: 1},
{o: 131.253, f: 141.227, l: "5", a: 0},
{o: 141.227, f: 146.12, l: "8", a: 1}],
[{o: 0, f: 27.565, l: "a", a: 0},
{o: 27.565, f: 57.365, l: "a", a: 1},
{o: 57.365, f: 66.305, l: "b", a: 0},
{o: 66.305, f: 90.145, l: "c", a: 1},
{o: 90.145, f: 116.965, l: "c", a: 0},
{o: 116.965, f: 146.02, l: "d", a: 1}],
[{o: 0, f: 78.623, l: "n1", a: 0},
{o: 78.623, f: 88.944, l: "A", a: 1},
{o: 88.944, f: 106.138, l: "n2", a: 0},
{o: 106.138, f: 116.367, l: "A", a: 1},
{o: 116.367, f: 146.425, l: "n3", a: 0}],
[{o: 0, f: 0.24, l: "G", a: 0},
{o: 0.24, f: 15.076, l: "E", a: 1},
{o: 15.076, f: 32.28, l: "A", a: 0},
{o: 32.28, f: 44.88, l: "C", a: 1},
{o: 44.88, f: 62.1, l: "A", a: 0},
{o: 62.1, f: 72.472, l: "B", a: 1},
{o: 72.472, f: 115.48, l: "D", a: 0},
{o: 115.48, f: 126.392, l: "B", a: 1},
{o: 126.392, f: 143.78, l: "F", a: 0},
{o: 143.78, f: 146.47, l: "G", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001234.ogg";

var artist = "Chava Alberstein";

var track = "Hul iet Hul iet Kinderlech Have A G";
