var data = [
[{o: 0.423, f: 10.237, l: "A", a: 0},
{o: 10.237, f: 19.851, l: "A", a: 1},
{o: 19.851, f: 29.524, l: "A", a: 0},
{o: 29.524, f: 39.158, l: "A", a: 1},
{o: 39.158, f: 48.942, l: "B", a: 0},
{o: 48.942, f: 58.59, l: "A", a: 1},
{o: 58.59, f: 68.192, l: "A", a: 0},
{o: 68.192, f: 78.03, l: "B", a: 1},
{o: 78.03, f: 97.965, l: "A", a: 0}],
[{o: 0.012, f: 8.028, l: "A", a: 0},
{o: 8.028, f: 19.464, l: "D", a: 1},
{o: 19.464, f: 30.936, l: "D", a: 0},
{o: 30.936, f: 39.708, l: "D", a: 1},
{o: 39.708, f: 47.66, l: "D", a: 0},
{o: 47.66, f: 60.9, l: "D", a: 1},
{o: 60.9, f: 70.268, l: "D", a: 0},
{o: 70.268, f: 76.76, l: "D", a: 1},
{o: 76.76, f: 86.464, l: "D", a: 0},
{o: 86.464, f: 95.912, l: "D", a: 1}],
[{o: 0.012, f: 8.028, l: "A", a: 0},
{o: 8.028, f: 19.464, l: "D", a: 1},
{o: 19.464, f: 30.936, l: "B", a: 0},
{o: 30.936, f: 39.708, l: "B", a: 1},
{o: 39.708, f: 47.66, l: "C", a: 0},
{o: 47.66, f: 60.9, l: "B", a: 1},
{o: 60.9, f: 70.268, l: "D", a: 0},
{o: 70.268, f: 76.76, l: "C", a: 1},
{o: 76.76, f: 86.464, l: "B", a: 0},
{o: 86.464, f: 95.912, l: "E", a: 1}],
[{o: 0.44, f: 8.613, l: "8", a: 0},
{o: 8.613, f: 18.24, l: "1", a: 1},
{o: 18.24, f: 36.987, l: "2", a: 0},
{o: 36.987, f: 47.347, l: "4", a: 1},
{o: 47.347, f: 56.36, l: "7", a: 0},
{o: 56.36, f: 66.627, l: "1", a: 1},
{o: 66.627, f: 75.84, l: "6", a: 0},
{o: 75.84, f: 87.387, l: "3", a: 1},
{o: 87.387, f: 96.227, l: "5", a: 0}],
[{o: 0, f: 10.43, l: "a", a: 0},
{o: 10.43, f: 18.625, l: "a", a: 1},
{o: 18.625, f: 29.8, l: "b", a: 0},
{o: 29.8, f: 40.23, l: "a", a: 1},
{o: 40.23, f: 46.935, l: "c", a: 0},
{o: 46.935, f: 58.11, l: "b", a: 1},
{o: 58.11, f: 68.54, l: "a", a: 0},
{o: 68.54, f: 75.99, l: "c", a: 1},
{o: 75.99, f: 96.85, l: "d", a: 0}],
[{o: 0, f: 1.033, l: "n1", a: 0},
{o: 1.033, f: 10.774, l: "A", a: 1},
{o: 10.774, f: 20.41, l: "A", a: 0},
{o: 20.41, f: 30.07, l: "A", a: 1},
{o: 30.07, f: 38.522, l: "A", a: 0},
{o: 38.522, f: 49.505, l: "B", a: 1},
{o: 49.505, f: 59.141, l: "A", a: 0},
{o: 59.141, f: 67.558, l: "A", a: 1},
{o: 67.558, f: 78.611, l: "B", a: 0},
{o: 78.611, f: 87.133, l: "A", a: 1},
{o: 87.133, f: 97.756, l: "n8", a: 0}],
[{o: 0, f: 0.012, l: "I", a: 0},
{o: 0.012, f: 9.996, l: "F", a: 1},
{o: 9.996, f: 19.168, l: "B", a: 0},
{o: 19.168, f: 31.088, l: "H", a: 1},
{o: 31.088, f: 40.82, l: "E", a: 0},
{o: 40.82, f: 49.168, l: "C", a: 1},
{o: 49.168, f: 69.684, l: "B", a: 0},
{o: 69.684, f: 82.064, l: "C", a: 1},
{o: 82.064, f: 87.412, l: "G", a: 0},
{o: 87.412, f: 95.456, l: "A", a: 1},
{o: 95.456, f: 97.96, l: "I", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000355.ogg";

var artist = "Bob Dylan";

var track = "Country Pie";
