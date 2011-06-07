var data = [
[{o: 0.163, f: 10.721, l: "A", a: 0},
{o: 10.721, f: 21.288, l: "A", a: 1},
{o: 21.288, f: 31.664, l: "A", a: 0},
{o: 31.664, f: 42.195, l: "A", a: 1},
{o: 42.195, f: 52.702, l: "B", a: 0},
{o: 52.702, f: 63.136, l: "A", a: 1},
{o: 63.136, f: 73.619, l: "A", a: 0},
{o: 73.619, f: 84.096, l: "A", a: 1},
{o: 84.096, f: 94.686, l: "A", a: 0},
{o: 94.686, f: 105.086, l: "B", a: 1},
{o: 105.086, f: 115.589, l: "A", a: 0},
{o: 115.589, f: 126.173, l: "A", a: 1},
{o: 126.173, f: 136.621, l: "A", a: 0},
{o: 136.621, f: 147.01, l: "A", a: 1},
{o: 147.01, f: 157.582, l: "A", a: 0},
{o: 157.582, f: 168.062, l: "B", a: 1},
{o: 168.062, f: 178.574, l: "A", a: 0},
{o: 178.574, f: 189.047, l: "A", a: 1},
{o: 189.047, f: 199.557, l: "A", a: 0},
{o: 199.557, f: 217.998, l: "C", a: 1},
{o: 217.998, f: 238.414, l: "Z", a: 0}],
[{o: 0.108, f: 9.94, l: "A", a: 0},
{o: 9.94, f: 25.348, l: "A", a: 1},
{o: 25.348, f: 38.788, l: "A", a: 0},
{o: 38.788, f: 52.852, l: "A", a: 1},
{o: 52.852, f: 63.708, l: "A", a: 0},
{o: 63.708, f: 75.508, l: "A", a: 1},
{o: 75.508, f: 91.248, l: "A", a: 0},
{o: 91.248, f: 105.02, l: "A", a: 1},
{o: 105.02, f: 126.008, l: "A", a: 0},
{o: 126.008, f: 138.132, l: "A", a: 1},
{o: 138.132, f: 147.964, l: "A", a: 0},
{o: 147.964, f: 158.132, l: "A", a: 1},
{o: 158.132, f: 167.972, l: "A", a: 0},
{o: 167.972, f: 184.044, l: "A", a: 1},
{o: 184.044, f: 199.792, l: "A", a: 0},
{o: 199.792, f: 209.952, l: "B", a: 1},
{o: 209.952, f: 217.764, l: "B", a: 0},
{o: 217.764, f: 235.724, l: "A", a: 1}],
[{o: 0.108, f: 9.94, l: "A", a: 0},
{o: 9.94, f: 25.348, l: "C", a: 1},
{o: 25.348, f: 38.788, l: "C", a: 0},
{o: 38.788, f: 52.852, l: "C", a: 1},
{o: 52.852, f: 63.708, l: "C", a: 0},
{o: 63.708, f: 75.508, l: "C", a: 1},
{o: 75.508, f: 91.248, l: "C", a: 0},
{o: 91.248, f: 105.02, l: "C", a: 1},
{o: 105.02, f: 126.008, l: "C", a: 0},
{o: 126.008, f: 138.132, l: "B", a: 1},
{o: 138.132, f: 147.964, l: "C", a: 0},
{o: 147.964, f: 158.132, l: "D", a: 1},
{o: 158.132, f: 167.972, l: "E", a: 0},
{o: 167.972, f: 184.044, l: "C", a: 1},
{o: 184.044, f: 199.792, l: "A", a: 0},
{o: 199.792, f: 209.952, l: "F", a: 1},
{o: 209.952, f: 217.764, l: "G", a: 0},
{o: 217.764, f: 235.724, l: "H", a: 1}],
[{o: 0.813, f: 10.267, l: "7", a: 0},
{o: 10.267, f: 45.027, l: "1", a: 1},
{o: 45.027, f: 52.227, l: "3", a: 0},
{o: 52.227, f: 63.387, l: "2", a: 1},
{o: 63.387, f: 94.2, l: "1", a: 0},
{o: 94.2, f: 125.013, l: "2", a: 1},
{o: 125.013, f: 145.36, l: "1", a: 0},
{o: 145.36, f: 168.293, l: "3", a: 1},
{o: 168.293, f: 193.24, l: "2", a: 0},
{o: 193.24, f: 207.987, l: "4", a: 1},
{o: 207.987, f: 218.28, l: "6", a: 0},
{o: 218.28, f: 230.08, l: "5", a: 1},
{o: 230.08, f: 238.253, l: "8", a: 0}],
[{o: 0, f: 68.54, l: "a", a: 0},
{o: 68.54, f: 79.715, l: "b", a: 1},
{o: 79.715, f: 96.85, l: "b", a: 0},
{o: 96.85, f: 108.025, l: "c", a: 1},
{o: 108.025, f: 176.565, l: "a", a: 0},
{o: 176.565, f: 196.68, l: "b", a: 1},
{o: 196.68, f: 237.655, l: "d", a: 0}],
[{o: 0, f: 24.067, l: "n1", a: 0},
{o: 24.067, f: 34.563, l: "C", a: 1},
{o: 34.563, f: 51.931, l: "n2", a: 0},
{o: 51.931, f: 71.274, l: "A", a: 1},
{o: 71.274, f: 81.769, l: "C", a: 0},
{o: 81.769, f: 104.385, l: "n4", a: 1},
{o: 104.385, f: 120.14, l: "A", a: 0},
{o: 120.14, f: 131.924, l: "B", a: 1},
{o: 131.924, f: 167.346, l: "n5", a: 0},
{o: 167.346, f: 183.089, l: "A", a: 1},
{o: 183.089, f: 194.885, l: "B", a: 0},
{o: 194.885, f: 238.19, l: "n6", a: 1}],
[{o: 0, f: 0.108, l: "E", a: 0},
{o: 0.108, f: 0.108, l: "B", a: 1},
{o: 0.108, f: 189.608, l: "A", a: 0},
{o: 189.608, f: 216.188, l: "D", a: 1},
{o: 216.188, f: 235.724, l: "B", a: 0},
{o: 235.724, f: 238.347, l: "E", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000570.ogg";
