var data = [
[{o: 0.084, f: 10.319, l: "A", a: 0},
{o: 10.319, f: 20.55, l: "B", a: 1},
{o: 20.55, f: 30.87, l: "C", a: 0},
{o: 30.87, f: 41.014, l: "A", a: 1},
{o: 41.014, f: 51.2, l: "B", a: 0},
{o: 51.2, f: 61.36, l: "C", a: 1},
{o: 61.36, f: 81.79, l: "D", a: 0},
{o: 81.79, f: 92.054, l: "B", a: 1},
{o: 92.054, f: 102.334, l: "C", a: 0},
{o: 102.334, f: 112.438, l: "A", a: 1},
{o: 112.438, f: 122.703, l: "B", a: 0},
{o: 122.703, f: 132.87, l: "C", a: 1},
{o: 132.87, f: 153.366, l: "D", a: 0},
{o: 153.366, f: 163.55, l: "B", a: 1},
{o: 163.55, f: 176.375, l: "C", a: 0},
{o: 176.375, f: 194.361, l: "A", a: 1}],
[{o: 0.02, f: 10.264, l: "A", a: 0},
{o: 10.264, f: 19.516, l: "C", a: 1},
{o: 19.516, f: 29.104, l: "C", a: 0},
{o: 29.104, f: 40.272, l: "A", a: 1},
{o: 40.272, f: 49.656, l: "C", a: 0},
{o: 49.656, f: 60.376, l: "C", a: 1},
{o: 60.376, f: 77.78, l: "C", a: 0},
{o: 77.78, f: 88.18, l: "C", a: 1},
{o: 88.18, f: 101.236, l: "C", a: 0},
{o: 101.236, f: 112.208, l: "A", a: 1},
{o: 112.208, f: 133.008, l: "C", a: 0},
{o: 133.008, f: 148.624, l: "B", a: 1},
{o: 148.624, f: 162.484, l: "D", a: 0},
{o: 162.484, f: 177.496, l: "E", a: 1},
{o: 177.496, f: 187.532, l: "F", a: 0}],
[{o: 0.02, f: 10.264, l: "A", a: 0},
{o: 10.264, f: 19.516, l: "C", a: 1},
{o: 19.516, f: 29.104, l: "C", a: 0},
{o: 29.104, f: 40.272, l: "A", a: 1},
{o: 40.272, f: 49.656, l: "C", a: 0},
{o: 49.656, f: 60.376, l: "B", a: 1},
{o: 60.376, f: 77.78, l: "D", a: 0},
{o: 77.78, f: 88.18, l: "E", a: 1},
{o: 88.18, f: 101.236, l: "C", a: 0},
{o: 101.236, f: 112.208, l: "A", a: 1},
{o: 112.208, f: 133.008, l: "C", a: 0},
{o: 133.008, f: 148.624, l: "F", a: 1},
{o: 148.624, f: 162.484, l: "G", a: 0},
{o: 162.484, f: 177.496, l: "H", a: 1},
{o: 177.496, f: 187.532, l: "I", a: 0}],
[{o: 0.547, f: 2.133, l: "8", a: 0},
{o: 2.133, f: 10.893, l: "2", a: 1},
{o: 10.893, f: 25.587, l: "1", a: 0},
{o: 25.587, f: 41.093, l: "2", a: 1},
{o: 41.093, f: 61.027, l: "1", a: 0},
{o: 61.027, f: 82.027, l: "3", a: 1},
{o: 82.027, f: 97.08, l: "1", a: 0},
{o: 97.08, f: 112.387, l: "2", a: 1},
{o: 112.387, f: 132.52, l: "1", a: 0},
{o: 132.52, f: 151.533, l: "3", a: 1},
{o: 151.533, f: 165.373, l: "4", a: 0},
{o: 165.373, f: 178.787, l: "5", a: 1},
{o: 178.787, f: 186.493, l: "7", a: 0},
{o: 186.493, f: 194.067, l: "6", a: 1}],
[{o: 0, f: 26.82, l: "a", a: 0},
{o: 26.82, f: 58.11, l: "a", a: 1},
{o: 58.11, f: 99.83, l: "b", a: 0},
{o: 99.83, f: 129.63, l: "a", a: 1},
{o: 129.63, f: 171.35, l: "b", a: 0},
{o: 171.35, f: 193.7, l: "c", a: 1}],
[{o: 0, f: 9.009, l: "B", a: 0},
{o: 9.009, f: 30.743, l: "A", a: 1},
{o: 30.743, f: 39.66, l: "B", a: 0},
{o: 39.66, f: 60.082, l: "A", a: 1},
{o: 60.082, f: 70.275, l: "C", a: 0},
{o: 70.275, f: 80.504, l: "C", a: 1},
{o: 80.504, f: 102.04, l: "A", a: 0},
{o: 102.04, f: 110.957, l: "B", a: 1},
{o: 110.957, f: 131.541, l: "A", a: 0},
{o: 131.541, f: 141.769, l: "C", a: 1},
{o: 141.769, f: 151.684, l: "C", a: 0},
{o: 151.684, f: 172.42, l: "A", a: 1},
{o: 172.42, f: 176.263, l: "n7", a: 0},
{o: 176.263, f: 185.191, l: "B", a: 1},
{o: 185.191, f: 194.351, l: "n8", a: 0}],
[{o: 0, f: 0.016, l: "F", a: 0},
{o: 0.016, f: 0.016, l: "B", a: 1},
{o: 0.016, f: 10.26, l: "A", a: 0},
{o: 10.26, f: 16.344, l: "B", a: 1},
{o: 16.344, f: 27.028, l: "E", a: 0},
{o: 27.028, f: 40.948, l: "A", a: 1},
{o: 40.948, f: 46.992, l: "B", a: 0},
{o: 46.992, f: 59.076, l: "E", a: 1},
{o: 59.076, f: 81.728, l: "D", a: 0},
{o: 81.728, f: 87.848, l: "B", a: 1},
{o: 87.848, f: 100.296, l: "E", a: 0},
{o: 100.296, f: 112.4, l: "A", a: 1},
{o: 112.4, f: 118.468, l: "B", a: 0},
{o: 118.468, f: 128.844, l: "E", a: 1},
{o: 128.844, f: 153.276, l: "D", a: 0},
{o: 153.276, f: 159.336, l: "B", a: 1},
{o: 159.336, f: 172.404, l: "E", a: 0},
{o: 172.404, f: 187.26, l: "A", a: 1},
{o: 187.26, f: 194.307, l: "F", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001497.ogg";
