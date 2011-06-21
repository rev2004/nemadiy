var data = [
[{o: 0.186, f: 6.554, l: "N", a: 0},
{o: 6.554, f: 12.318, l: "A", a: 1},
{o: 12.318, f: 24.024, l: "A", a: 0},
{o: 24.024, f: 35.776, l: "X", a: 1},
{o: 35.776, f: 53.304, l: "B", a: 0},
{o: 53.304, f: 73.16, l: "C", a: 1},
{o: 73.16, f: 84.825, l: "A", a: 0},
{o: 84.825, f: 96.505, l: "X", a: 1},
{o: 96.505, f: 114.059, l: "B", a: 0},
{o: 114.059, f: 133.991, l: "C", a: 1},
{o: 133.991, f: 145.641, l: "A", a: 0},
{o: 145.641, f: 157.24, l: "X", a: 1},
{o: 157.24, f: 174.727, l: "B", a: 0},
{o: 174.727, f: 203.106, l: "X", a: 1}],
[{o: 0.132, f: 6.488, l: "A", a: 0},
{o: 6.488, f: 20.364, l: "C", a: 1},
{o: 20.364, f: 31.7, l: "C", a: 0},
{o: 31.7, f: 43.464, l: "C", a: 1},
{o: 43.464, f: 52.896, l: "C", a: 0},
{o: 52.896, f: 74.552, l: "C", a: 1},
{o: 74.552, f: 94.296, l: "C", a: 0},
{o: 94.296, f: 104.196, l: "B", a: 1},
{o: 104.196, f: 124.928, l: "C", a: 0},
{o: 124.928, f: 134.304, l: "C", a: 1},
{o: 134.304, f: 158.304, l: "C", a: 0},
{o: 158.304, f: 174.672, l: "C", a: 1},
{o: 174.672, f: 187.736, l: "D", a: 0},
{o: 187.736, f: 195.24, l: "E", a: 1}],
[{o: 0.132, f: 6.488, l: "A", a: 0},
{o: 6.488, f: 20.364, l: "C", a: 1},
{o: 20.364, f: 31.7, l: "C", a: 0},
{o: 31.7, f: 43.464, l: "D", a: 1},
{o: 43.464, f: 52.896, l: "C", a: 0},
{o: 52.896, f: 74.552, l: "B", a: 1},
{o: 74.552, f: 94.296, l: "C", a: 0},
{o: 94.296, f: 104.196, l: "E", a: 1},
{o: 104.196, f: 124.928, l: "B", a: 0},
{o: 124.928, f: 134.304, l: "B", a: 1},
{o: 134.304, f: 158.304, l: "C", a: 0},
{o: 158.304, f: 174.672, l: "F", a: 1},
{o: 174.672, f: 187.736, l: "G", a: 0},
{o: 187.736, f: 195.24, l: "H", a: 1}],
[{o: 0.387, f: 5.24, l: "7", a: 0},
{o: 5.24, f: 14.32, l: "1", a: 1},
{o: 14.32, f: 20.92, l: "3", a: 0},
{o: 20.92, f: 27.12, l: "1", a: 1},
{o: 27.12, f: 32.28, l: "6", a: 0},
{o: 32.28, f: 44.773, l: "4", a: 1},
{o: 44.773, f: 50.933, l: "1", a: 0},
{o: 50.933, f: 63.96, l: "2", a: 1},
{o: 63.96, f: 72.933, l: "1", a: 0},
{o: 72.933, f: 81.707, l: "3", a: 1},
{o: 81.707, f: 87.88, l: "1", a: 0},
{o: 87.88, f: 95.56, l: "3", a: 1},
{o: 95.56, f: 105.507, l: "4", a: 0},
{o: 105.507, f: 124.387, l: "2", a: 1},
{o: 124.387, f: 133.773, l: "1", a: 0},
{o: 133.773, f: 141.8, l: "3", a: 1},
{o: 141.8, f: 148.667, l: "1", a: 0},
{o: 148.667, f: 156.68, l: "3", a: 1},
{o: 156.68, f: 166.173, l: "4", a: 0},
{o: 166.173, f: 173.773, l: "2", a: 1},
{o: 173.773, f: 183.92, l: "4", a: 0},
{o: 183.92, f: 191.573, l: "2", a: 1},
{o: 191.573, f: 199.627, l: "5", a: 0},
{o: 199.627, f: 202.013, l: "8", a: 1}],
[{o: 0, f: 23.84, l: "a", a: 0},
{o: 23.84, f: 49.915, l: "b", a: 1},
{o: 49.915, f: 68.54, l: "a", a: 0},
{o: 68.54, f: 92.38, l: "a", a: 1},
{o: 92.38, f: 111.005, l: "b", a: 0},
{o: 111.005, f: 130.375, l: "a", a: 1},
{o: 130.375, f: 154.215, l: "a", a: 0},
{o: 154.215, f: 172.84, l: "b", a: 1},
{o: 172.84, f: 189.23, l: "a", a: 0},
{o: 189.23, f: 202.64, l: "c", a: 1}],
[{o: 0, f: 13.781, l: "n1", a: 0},
{o: 13.781, f: 28.421, l: "B", a: 1},
{o: 28.421, f: 74.594, l: "A", a: 0},
{o: 74.594, f: 89.199, l: "B", a: 1},
{o: 89.199, f: 135.43, l: "A", a: 0},
{o: 135.43, f: 149.966, l: "n2", a: 1},
{o: 149.966, f: 197.509, l: "A", a: 0},
{o: 197.509, f: 202.896, l: "n3", a: 1}],
[{o: 0, f: 0.132, l: "E", a: 0},
{o: 0.132, f: 23.608, l: "D", a: 1},
{o: 23.608, f: 45.684, l: "B", a: 0},
{o: 45.684, f: 81.52, l: "C", a: 1},
{o: 81.52, f: 109.316, l: "B", a: 0},
{o: 109.316, f: 142.704, l: "C", a: 1},
{o: 142.704, f: 156.828, l: "B", a: 0},
{o: 156.828, f: 195.24, l: "A", a: 1},
{o: 195.24, f: 203.077, l: "E", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000513.ogg";

var artist = "Compilations";

var track = "Sweet Poison";
