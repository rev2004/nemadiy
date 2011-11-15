var data = [
[{o: 0.116, f: 101.694, l: "A", a: 0},
{o: 101.694, f: 140.672, l: "C", a: 1},
{o: 140.672, f: 174.772, l: "D", a: 0},
{o: 174.772, f: 213.057, l: "C", a: 1},
{o: 213.057, f: 241.087, l: "D", a: 0},
{o: 241.087, f: 265.935, l: "C", a: 1},
{o: 265.935, f: 306.028, l: "D", a: 0},
{o: 306.028, f: 322.273, l: "C", a: 1},
{o: 322.273, f: 370.492, l: "D", a: 0}],
[{o: 0.096, f: 14.988, l: "C", a: 0},
{o: 14.988, f: 37.608, l: "C", a: 1},
{o: 37.608, f: 49.072, l: "F", a: 0},
{o: 49.072, f: 66.196, l: "C", a: 1},
{o: 66.196, f: 79.728, l: "F", a: 0},
{o: 79.728, f: 101.944, l: "C", a: 1},
{o: 101.944, f: 123.532, l: "F", a: 0},
{o: 123.532, f: 135.06, l: "F", a: 1},
{o: 135.06, f: 162.872, l: "F", a: 0},
{o: 162.872, f: 183.716, l: "F", a: 1},
{o: 183.716, f: 194.864, l: "F", a: 0},
{o: 194.864, f: 207.416, l: "C", a: 1},
{o: 207.416, f: 230.012, l: "F", a: 0},
{o: 230.012, f: 245.332, l: "C", a: 1},
{o: 245.332, f: 261.348, l: "C", a: 0},
{o: 261.348, f: 286.02, l: "C", a: 1},
{o: 286.02, f: 305.86, l: "F", a: 0},
{o: 305.86, f: 319.144, l: "F", a: 1},
{o: 319.144, f: 334.792, l: "C", a: 0},
{o: 334.792, f: 352.876, l: "C", a: 1},
{o: 352.876, f: 370.376, l: "F", a: 0}],
[{o: 0.096, f: 14.988, l: "C", a: 0},
{o: 14.988, f: 37.608, l: "C", a: 1},
{o: 37.608, f: 49.072, l: "F", a: 0},
{o: 49.072, f: 66.196, l: "G", a: 1},
{o: 66.196, f: 79.728, l: "F", a: 0},
{o: 79.728, f: 101.944, l: "E", a: 1},
{o: 101.944, f: 123.532, l: "B", a: 0},
{o: 123.532, f: 135.06, l: "A", a: 1},
{o: 135.06, f: 162.872, l: "A", a: 0},
{o: 162.872, f: 183.716, l: "B", a: 1},
{o: 183.716, f: 194.864, l: "B", a: 0},
{o: 194.864, f: 207.416, l: "H", a: 1},
{o: 207.416, f: 230.012, l: "A", a: 0},
{o: 230.012, f: 245.332, l: "I", a: 1},
{o: 245.332, f: 261.348, l: "J", a: 0},
{o: 261.348, f: 286.02, l: "E", a: 1},
{o: 286.02, f: 305.86, l: "A", a: 0},
{o: 305.86, f: 319.144, l: "B", a: 1},
{o: 319.144, f: 334.792, l: "D", a: 0},
{o: 334.792, f: 352.876, l: "D", a: 1},
{o: 352.876, f: 370.376, l: "B", a: 0}],
[{o: 0.467, f: 67.76, l: "4", a: 0},
{o: 67.76, f: 79.547, l: "6", a: 1},
{o: 79.547, f: 101.44, l: "2", a: 0},
{o: 101.44, f: 124.04, l: "3", a: 1},
{o: 124.04, f: 129.253, l: "1", a: 0},
{o: 129.253, f: 134.88, l: "3", a: 1},
{o: 134.88, f: 141.493, l: "1", a: 0},
{o: 141.493, f: 145.68, l: "2", a: 1},
{o: 145.68, f: 162.733, l: "1", a: 0},
{o: 162.733, f: 173.853, l: "2", a: 1},
{o: 173.853, f: 196.413, l: "3", a: 0},
{o: 196.413, f: 201.653, l: "1", a: 1},
{o: 201.653, f: 207.227, l: "3", a: 0},
{o: 207.227, f: 213.813, l: "1", a: 1},
{o: 213.813, f: 218, l: "2", a: 0},
{o: 218, f: 239.947, l: "1", a: 1},
{o: 239.947, f: 259.76, l: "5", a: 0},
{o: 259.76, f: 265.013, l: "7", a: 1},
{o: 265.013, f: 296.68, l: "2", a: 0},
{o: 296.68, f: 300.827, l: "1", a: 1},
{o: 300.827, f: 305.347, l: "2", a: 0},
{o: 305.347, f: 316.867, l: "3", a: 1},
{o: 316.867, f: 322.813, l: "1", a: 0},
{o: 322.813, f: 327.293, l: "2", a: 1},
{o: 327.293, f: 367.307, l: "1", a: 0},
{o: 367.307, f: 370.387, l: "8", a: 1}],
[{o: 0, f: 23.095, l: "a", a: 0},
{o: 23.095, f: 46.935, l: "a", a: 1},
{o: 46.935, f: 99.085, l: "b", a: 0},
{o: 99.085, f: 122.18, l: "a", a: 1},
{o: 122.18, f: 146.02, l: "a", a: 0},
{o: 146.02, f: 189.975, l: "b", a: 1},
{o: 189.975, f: 213.07, l: "a", a: 0},
{o: 213.07, f: 236.91, l: "a", a: 1},
{o: 236.91, f: 289.06, l: "b", a: 0},
{o: 289.06, f: 312.155, l: "a", a: 1},
{o: 312.155, f: 335.995, l: "a", a: 0},
{o: 335.995, f: 370.265, l: "b", a: 1}],
[{o: 0, f: 11.923, l: "n1", a: 0},
{o: 11.923, f: 27.214, l: "C", a: 1},
{o: 27.214, f: 48.762, l: "n2", a: 0},
{o: 48.762, f: 71.425, l: "B", a: 1},
{o: 71.425, f: 85.38, l: "D", a: 0},
{o: 85.38, f: 96.432, l: "A", a: 1},
{o: 96.432, f: 101.994, l: "n5", a: 0},
{o: 101.994, f: 113.104, l: "A", a: 1},
{o: 113.104, f: 124.947, l: "A", a: 0},
{o: 124.947, f: 140.655, l: "C", a: 1},
{o: 140.655, f: 163.294, l: "B", a: 0},
{o: 163.294, f: 174.417, l: "A", a: 1},
{o: 174.417, f: 185.493, l: "A", a: 0},
{o: 185.493, f: 197.3, l: "A", a: 1},
{o: 197.3, f: 212.985, l: "C", a: 0},
{o: 212.985, f: 233.883, l: "B", a: 1},
{o: 233.883, f: 241.209, l: "n11", a: 0},
{o: 241.209, f: 252.331, l: "A", a: 1},
{o: 252.331, f: 258.241, l: "n12", a: 0},
{o: 258.241, f: 269.421, l: "D", a: 1},
{o: 269.421, f: 305.888, l: "n13", a: 0},
{o: 305.888, f: 317.057, l: "A", a: 1},
{o: 317.057, f: 322.316, l: "n14", a: 0},
{o: 322.316, f: 343.191, l: "B", a: 1},
{o: 343.191, f: 348.009, l: "n15", a: 0},
{o: 348.009, f: 363.346, l: "C", a: 1},
{o: 363.346, f: 370.451, l: "n16", a: 0}],
[{o: 0, f: 0.004, l: "I", a: 0},
{o: 0.004, f: 4.088, l: "H", a: 1},
{o: 4.088, f: 68.596, l: "A", a: 0},
{o: 68.596, f: 79.176, l: "D", a: 1},
{o: 79.176, f: 101.084, l: "A", a: 0},
{o: 101.084, f: 123.372, l: "D", a: 1},
{o: 123.372, f: 184.584, l: "A", a: 0},
{o: 184.584, f: 197.628, l: "D", a: 1},
{o: 197.628, f: 272.508, l: "A", a: 0},
{o: 272.508, f: 305.516, l: "G", a: 1},
{o: 305.516, f: 317.896, l: "D", a: 0},
{o: 317.896, f: 338.78, l: "A", a: 1},
{o: 338.78, f: 346.92, l: "D", a: 0},
{o: 346.92, f: 356.348, l: "A", a: 1},
{o: 356.348, f: 363.828, l: "D", a: 0},
{o: 363.828, f: 370.42, l: "A", a: 1},
{o: 370.42, f: 370.47, l: "I", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000341.ogg";

var artist = "Apocalyptica";

var track = "Harvester of Sorrow";
