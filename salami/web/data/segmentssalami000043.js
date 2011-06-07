var data = [
[{o: 0.064, f: 4.34, l: "A", a: 0},
{o: 4.34, f: 30.963, l: "B", a: 1},
{o: 30.963, f: 57.203, l: "B", a: 0},
{o: 57.203, f: 85.287, l: "B", a: 1},
{o: 85.287, f: 115.52, l: "B", a: 0}],
[{o: 0.016, f: 3.884, l: "C", a: 0},
{o: 3.884, f: 12.384, l: "C", a: 1},
{o: 12.384, f: 16.588, l: "C", a: 0},
{o: 16.588, f: 27.548, l: "C", a: 1},
{o: 27.548, f: 35.06, l: "C", a: 0},
{o: 35.06, f: 45.196, l: "C", a: 1},
{o: 45.196, f: 55.148, l: "C", a: 0},
{o: 55.148, f: 63.152, l: "C", a: 1},
{o: 63.152, f: 73.66, l: "C", a: 0},
{o: 73.66, f: 81.42, l: "C", a: 1},
{o: 81.42, f: 91.636, l: "B", a: 0},
{o: 91.636, f: 96.988, l: "B", a: 1},
{o: 96.988, f: 102.42, l: "C", a: 0},
{o: 102.42, f: 112.504, l: "C", a: 1}],
[{o: 0.016, f: 3.884, l: "B", a: 0},
{o: 3.884, f: 12.384, l: "A", a: 1},
{o: 12.384, f: 16.588, l: "A", a: 0},
{o: 16.588, f: 27.548, l: "C", a: 1},
{o: 27.548, f: 35.06, l: "C", a: 0},
{o: 35.06, f: 45.196, l: "D", a: 1},
{o: 45.196, f: 55.148, l: "C", a: 0},
{o: 55.148, f: 63.152, l: "D", a: 1},
{o: 63.152, f: 73.66, l: "A", a: 0},
{o: 73.66, f: 81.42, l: "E", a: 1},
{o: 81.42, f: 91.636, l: "F", a: 0},
{o: 91.636, f: 96.988, l: "G", a: 1},
{o: 96.988, f: 102.42, l: "C", a: 0},
{o: 102.42, f: 112.504, l: "A", a: 1}],
[{o: 0.6, f: 4.107, l: "8", a: 0},
{o: 4.107, f: 14.493, l: "1", a: 1},
{o: 14.493, f: 18.307, l: "3", a: 0},
{o: 18.307, f: 28.187, l: "1", a: 1},
{o: 28.187, f: 54.533, l: "2", a: 0},
{o: 54.533, f: 59.48, l: "6", a: 1},
{o: 59.48, f: 70.573, l: "4", a: 0},
{o: 70.573, f: 81.213, l: "5", a: 1},
{o: 81.213, f: 94.653, l: "1", a: 0},
{o: 94.653, f: 98.587, l: "3", a: 1},
{o: 98.587, f: 111.707, l: "1", a: 0},
{o: 111.707, f: 115.427, l: "7", a: 1}],
[{o: 0, f: 15.645, l: "a", a: 0},
{o: 15.645, f: 29.8, l: "b", a: 1},
{o: 29.8, f: 42.465, l: "c", a: 0},
{o: 42.465, f: 56.62, l: "b", a: 1},
{o: 56.62, f: 68.54, l: "c", a: 0},
{o: 68.54, f: 96.85, l: "d", a: 1},
{o: 96.85, f: 114.73, l: "b", a: 0}],
[{o: 0, f: 68.719, l: "n1", a: 0},
{o: 68.719, f: 81.862, l: "A", a: 1},
{o: 81.862, f: 96.049, l: "n2", a: 0},
{o: 96.049, f: 108.484, l: "A", a: 1},
{o: 108.484, f: 115.217, l: "n3", a: 0}],
[{o: 0, f: 0.016, l: "H", a: 0},
{o: 0.016, f: 12.188, l: "D", a: 1},
{o: 12.188, f: 21.644, l: "B", a: 0},
{o: 21.644, f: 31.808, l: "E", a: 1},
{o: 31.808, f: 47.668, l: "B", a: 0},
{o: 47.668, f: 61.94, l: "F", a: 1},
{o: 61.94, f: 74.068, l: "G", a: 0},
{o: 74.068, f: 84.044, l: "F", a: 1},
{o: 84.044, f: 93.212, l: "D", a: 0},
{o: 93.212, f: 100.808, l: "B", a: 1},
{o: 100.808, f: 112.504, l: "E", a: 0},
{o: 112.504, f: 115.514, l: "H", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000043.ogg";
