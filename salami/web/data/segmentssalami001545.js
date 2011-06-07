var data = [
[{o: 0.721, f: 7.511, l: "Z", a: 0},
{o: 7.511, f: 24.071, l: "A", a: 1},
{o: 24.071, f: 74.089, l: "B", a: 0},
{o: 74.089, f: 111.999, l: "B", a: 1},
{o: 111.999, f: 157.564, l: "C", a: 0},
{o: 157.564, f: 202.584, l: "B", a: 1},
{o: 202.584, f: 251.362, l: "B", a: 0}],
[{o: 0.208, f: 7.416, l: "B", a: 0},
{o: 7.416, f: 18.604, l: "A", a: 1},
{o: 18.604, f: 27.892, l: "A", a: 0},
{o: 27.892, f: 43.524, l: "A", a: 1},
{o: 43.524, f: 53.188, l: "A", a: 0},
{o: 53.188, f: 67.292, l: "A", a: 1},
{o: 67.292, f: 77.684, l: "A", a: 0},
{o: 77.684, f: 88.972, l: "A", a: 1},
{o: 88.972, f: 101.02, l: "A", a: 0},
{o: 101.02, f: 110.584, l: "D", a: 1},
{o: 110.584, f: 117.336, l: "C", a: 0},
{o: 117.336, f: 128.104, l: "C", a: 1},
{o: 128.104, f: 141.224, l: "C", a: 0},
{o: 141.224, f: 154.452, l: "A", a: 1},
{o: 154.452, f: 173.728, l: "A", a: 0},
{o: 173.728, f: 184.952, l: "A", a: 1},
{o: 184.952, f: 201.296, l: "A", a: 0},
{o: 201.296, f: 214.676, l: "E", a: 1},
{o: 214.676, f: 230.088, l: "A", a: 0},
{o: 230.088, f: 237.344, l: "F", a: 1},
{o: 237.344, f: 248.528, l: "G", a: 0}],
[{o: 0.208, f: 7.416, l: "C", a: 0},
{o: 7.416, f: 18.604, l: "D", a: 1},
{o: 18.604, f: 27.892, l: "E", a: 0},
{o: 27.892, f: 43.524, l: "B", a: 1},
{o: 43.524, f: 53.188, l: "B", a: 0},
{o: 53.188, f: 67.292, l: "B", a: 1},
{o: 67.292, f: 77.684, l: "B", a: 0},
{o: 77.684, f: 88.972, l: "A", a: 1},
{o: 88.972, f: 101.02, l: "A", a: 0},
{o: 101.02, f: 110.584, l: "F", a: 1},
{o: 110.584, f: 117.336, l: "G", a: 0},
{o: 117.336, f: 128.104, l: "H", a: 1},
{o: 128.104, f: 141.224, l: "I", a: 0},
{o: 141.224, f: 154.452, l: "B", a: 1},
{o: 154.452, f: 173.728, l: "A", a: 0},
{o: 173.728, f: 184.952, l: "J", a: 1},
{o: 184.952, f: 201.296, l: "B", a: 0},
{o: 201.296, f: 214.676, l: "K", a: 1},
{o: 214.676, f: 230.088, l: "A", a: 0},
{o: 230.088, f: 237.344, l: "L", a: 1},
{o: 237.344, f: 248.528, l: "M", a: 0}],
[{o: 0.493, f: 6.96, l: "7", a: 0},
{o: 6.96, f: 16.547, l: "2", a: 1},
{o: 16.547, f: 20.067, l: "5", a: 0},
{o: 20.067, f: 33.8, l: "1", a: 1},
{o: 33.8, f: 37.293, l: "5", a: 0},
{o: 37.293, f: 41.44, l: "2", a: 1},
{o: 41.44, f: 51.453, l: "1", a: 0},
{o: 51.453, f: 54.947, l: "5", a: 1},
{o: 54.947, f: 66.813, l: "1", a: 0},
{o: 66.813, f: 86.6, l: "2", a: 1},
{o: 86.6, f: 94.547, l: "1", a: 0},
{o: 94.547, f: 99.933, l: "3", a: 1},
{o: 99.933, f: 111.107, l: "1", a: 0},
{o: 111.107, f: 115.293, l: "6", a: 1},
{o: 115.293, f: 119.093, l: "3", a: 0},
{o: 119.093, f: 123.213, l: "6", a: 1},
{o: 123.213, f: 134.373, l: "4", a: 0},
{o: 134.373, f: 140.76, l: "3", a: 1},
{o: 140.76, f: 150.813, l: "4", a: 0},
{o: 150.813, f: 171.373, l: "2", a: 1},
{o: 171.373, f: 180.12, l: "1", a: 0},
{o: 180.12, f: 185.733, l: "2", a: 1},
{o: 185.733, f: 191.12, l: "1", a: 0},
{o: 191.12, f: 202.067, l: "2", a: 1},
{o: 202.067, f: 214.2, l: "3", a: 0},
{o: 214.2, f: 223.653, l: "1", a: 1},
{o: 223.653, f: 228.693, l: "2", a: 0},
{o: 228.693, f: 240.12, l: "1", a: 1},
{o: 240.12, f: 243.787, l: "2", a: 0},
{o: 243.787, f: 249.907, l: "8", a: 1}],
[{o: 0, f: 12.665, l: "a", a: 0},
{o: 12.665, f: 32.035, l: "b", a: 1},
{o: 32.035, f: 70.775, l: "c", a: 0},
{o: 70.775, f: 122.925, l: "c", a: 1},
{o: 122.925, f: 149, l: "d", a: 0},
{o: 149, f: 168.37, l: "b", a: 1},
{o: 168.37, f: 207.11, l: "c", a: 0},
{o: 207.11, f: 251.065, l: "c", a: 1}],
[{o: 0, f: 16.684, l: "n1", a: 0},
{o: 16.684, f: 25.693, l: "A", a: 1},
{o: 25.693, f: 66.688, l: "n2", a: 0},
{o: 66.688, f: 75.546, l: "A", a: 1},
{o: 75.546, f: 150.361, l: "n3", a: 0},
{o: 150.361, f: 159.242, l: "A", a: 1},
{o: 159.242, f: 251.193, l: "n4", a: 0}],
[{o: 0, f: 0.356, l: "H", a: 0},
{o: 0.356, f: 18.292, l: "C", a: 1},
{o: 18.292, f: 37.432, l: "A", a: 0},
{o: 37.432, f: 56.056, l: "G", a: 1},
{o: 56.056, f: 104.268, l: "A", a: 0},
{o: 104.268, f: 149.364, l: "G", a: 1},
{o: 149.364, f: 232.616, l: "A", a: 0},
{o: 232.616, f: 248.532, l: "F", a: 1},
{o: 248.532, f: 251.299, l: "H", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001545.ogg";
