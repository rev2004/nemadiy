var data = [
[{o: 0.383, f: 13.682, l: "A", a: 0},
{o: 13.682, f: 26.993, l: "A", a: 1},
{o: 26.993, f: 40.471, l: "B", a: 0},
{o: 40.471, f: 53.785, l: "B", a: 1},
{o: 53.785, f: 67.482, l: "A", a: 0},
{o: 67.482, f: 80.562, l: "C", a: 1},
{o: 80.562, f: 93.505, l: "C", a: 0},
{o: 93.505, f: 107.307, l: "A", a: 1},
{o: 107.307, f: 126.015, l: "A", a: 0}],
[{o: 0.008, f: 12.808, l: "D", a: 0},
{o: 12.808, f: 26.54, l: "D", a: 1},
{o: 26.54, f: 39.168, l: "E", a: 0},
{o: 39.168, f: 52.516, l: "E", a: 1},
{o: 52.516, f: 66.108, l: "D", a: 0},
{o: 66.108, f: 73.216, l: "D", a: 1},
{o: 73.216, f: 79.34, l: "D", a: 0},
{o: 79.34, f: 85.004, l: "E", a: 1},
{o: 85.004, f: 96.168, l: "E", a: 0},
{o: 96.168, f: 102.544, l: "D", a: 1},
{o: 102.544, f: 113.24, l: "D", a: 0},
{o: 113.24, f: 124.144, l: "D", a: 1}],
[{o: 0.008, f: 12.808, l: "D", a: 0},
{o: 12.808, f: 26.54, l: "A", a: 1},
{o: 26.54, f: 39.168, l: "E", a: 0},
{o: 39.168, f: 52.516, l: "E", a: 1},
{o: 52.516, f: 66.108, l: "A", a: 0},
{o: 66.108, f: 73.216, l: "C", a: 1},
{o: 73.216, f: 79.34, l: "F", a: 0},
{o: 79.34, f: 85.004, l: "E", a: 1},
{o: 85.004, f: 96.168, l: "E", a: 0},
{o: 96.168, f: 102.544, l: "B", a: 1},
{o: 102.544, f: 113.24, l: "B", a: 0},
{o: 113.24, f: 124.144, l: "D", a: 1}],
[{o: 0.387, f: 11.347, l: "3", a: 0},
{o: 11.347, f: 26.84, l: "1", a: 1},
{o: 26.84, f: 42.733, l: "2", a: 0},
{o: 42.733, f: 51.48, l: "4", a: 1},
{o: 51.48, f: 66.44, l: "1", a: 0},
{o: 66.44, f: 72.6, l: "6", a: 1},
{o: 72.6, f: 77.893, l: "7", a: 0},
{o: 77.893, f: 85.213, l: "5", a: 1},
{o: 85.213, f: 122.413, l: "1", a: 0},
{o: 122.413, f: 125.747, l: "8", a: 1}],
[{o: 0, f: 8.195, l: "a", a: 0},
{o: 8.195, f: 28.31, l: "b", a: 1},
{o: 28.31, f: 52.15, l: "c", a: 0},
{o: 52.15, f: 68.54, l: "b", a: 1},
{o: 68.54, f: 102.065, l: "c", a: 0},
{o: 102.065, f: 125.16, l: "b", a: 1}],
[{o: 0, f: 0.464, l: "n1", a: 0},
{o: 0.464, f: 13.688, l: "A", a: 1},
{o: 13.688, f: 27.04, l: "A", a: 0},
{o: 27.04, f: 40.496, l: "B", a: 1},
{o: 40.496, f: 53.882, l: "B", a: 0},
{o: 53.882, f: 67.466, l: "A", a: 1},
{o: 67.466, f: 80.643, l: "C", a: 0},
{o: 80.643, f: 93.716, l: "C", a: 1},
{o: 93.716, f: 107.288, l: "A", a: 0},
{o: 107.288, f: 120.488, l: "A", a: 1},
{o: 120.488, f: 125.991, l: "n2", a: 0}],
[{o: 0, f: 0.008, l: "D", a: 0},
{o: 0.008, f: 27.868, l: "C", a: 1},
{o: 27.868, f: 52.944, l: "A", a: 0},
{o: 52.944, f: 69.112, l: "C", a: 1},
{o: 69.112, f: 92.832, l: "A", a: 0},
{o: 92.832, f: 124.144, l: "C", a: 1},
{o: 124.144, f: 126.016, l: "D", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000618.ogg";