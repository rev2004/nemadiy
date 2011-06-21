var data = [
[{o: 0.139, f: 13.413, l: "C", a: 0},
{o: 13.413, f: 39.907, l: "A", a: 1},
{o: 39.907, f: 48.605, l: "C", a: 0},
{o: 48.605, f: 74.809, l: "A", a: 1},
{o: 74.809, f: 83.533, l: "C", a: 0},
{o: 83.533, f: 211.795, l: "A", a: 1},
{o: 211.795, f: 220.199, l: "C", a: 0},
{o: 220.199, f: 312.768, l: "A", a: 1},
{o: 312.768, f: 321.381, l: "C", a: 0},
{o: 321.381, f: 380.692, l: "A", a: 1},
{o: 380.692, f: 389.238, l: "C", a: 0},
{o: 389.238, f: 415.138, l: "A", a: 1},
{o: 415.138, f: 423.544, l: "C", a: 0},
{o: 423.544, f: 461.635, l: "A", a: 1}],
[{o: 0.004, f: 17.52, l: "A", a: 0},
{o: 17.52, f: 39.78, l: "B", a: 1},
{o: 39.78, f: 54.052, l: "E", a: 0},
{o: 54.052, f: 75.468, l: "B", a: 1},
{o: 75.468, f: 99.708, l: "D", a: 0},
{o: 99.708, f: 124.052, l: "D", a: 1},
{o: 124.052, f: 148.56, l: "D", a: 0},
{o: 148.56, f: 177.932, l: "D", a: 1},
{o: 177.932, f: 201.216, l: "D", a: 0},
{o: 201.216, f: 221.22, l: "D", a: 1},
{o: 221.22, f: 236.172, l: "D", a: 0},
{o: 236.172, f: 248.316, l: "D", a: 1},
{o: 248.316, f: 272.908, l: "D", a: 0},
{o: 272.908, f: 293.592, l: "D", a: 1},
{o: 293.592, f: 312.984, l: "F", a: 0},
{o: 312.984, f: 330.148, l: "C", a: 1},
{o: 330.148, f: 363.636, l: "C", a: 0},
{o: 363.636, f: 387.692, l: "G", a: 1},
{o: 387.692, f: 412.324, l: "H", a: 0},
{o: 412.324, f: 438.508, l: "I", a: 1},
{o: 438.508, f: 456.816, l: "J", a: 0}],
[{o: 0.004, f: 17.52, l: "E", a: 0},
{o: 17.52, f: 39.78, l: "B", a: 1},
{o: 39.78, f: 54.052, l: "F", a: 0},
{o: 54.052, f: 75.468, l: "B", a: 1},
{o: 75.468, f: 99.708, l: "D", a: 0},
{o: 99.708, f: 124.052, l: "D", a: 1},
{o: 124.052, f: 148.56, l: "D", a: 0},
{o: 148.56, f: 177.932, l: "D", a: 1},
{o: 177.932, f: 201.216, l: "G", a: 0},
{o: 201.216, f: 221.22, l: "H", a: 1},
{o: 221.22, f: 236.172, l: "I", a: 0},
{o: 236.172, f: 248.316, l: "A", a: 1},
{o: 248.316, f: 272.908, l: "A", a: 0},
{o: 272.908, f: 293.592, l: "A", a: 1},
{o: 293.592, f: 312.984, l: "J", a: 0},
{o: 312.984, f: 330.148, l: "C", a: 1},
{o: 330.148, f: 363.636, l: "C", a: 0},
{o: 363.636, f: 387.692, l: "K", a: 1},
{o: 387.692, f: 412.324, l: "L", a: 0},
{o: 412.324, f: 438.508, l: "M", a: 1},
{o: 438.508, f: 456.816, l: "N", a: 0}],
[{o: 0.547, f: 4.707, l: "8", a: 0},
{o: 4.707, f: 12.32, l: "1", a: 1},
{o: 12.32, f: 20.56, l: "3", a: 0},
{o: 20.56, f: 38.92, l: "2", a: 1},
{o: 38.92, f: 47.507, l: "1", a: 0},
{o: 47.507, f: 56.347, l: "3", a: 1},
{o: 56.347, f: 73.933, l: "2", a: 0},
{o: 73.933, f: 85.72, l: "1", a: 1},
{o: 85.72, f: 92.24, l: "4", a: 0},
{o: 92.24, f: 99.267, l: "1", a: 1},
{o: 99.267, f: 109.053, l: "4", a: 0},
{o: 109.053, f: 144.12, l: "1", a: 1},
{o: 144.12, f: 150.573, l: "6", a: 0},
{o: 150.573, f: 177.16, l: "1", a: 1},
{o: 177.16, f: 185.2, l: "6", a: 0},
{o: 185.2, f: 199.373, l: "4", a: 1},
{o: 199.373, f: 215.08, l: "1", a: 0},
{o: 215.08, f: 225.533, l: "3", a: 1},
{o: 225.533, f: 244.307, l: "2", a: 0},
{o: 244.307, f: 250.667, l: "5", a: 1},
{o: 250.667, f: 271.08, l: "2", a: 0},
{o: 271.08, f: 282.88, l: "3", a: 1},
{o: 282.88, f: 293.56, l: "5", a: 0},
{o: 293.56, f: 312.12, l: "2", a: 1},
{o: 312.12, f: 387.68, l: "1", a: 0},
{o: 387.68, f: 396.827, l: "3", a: 1},
{o: 396.827, f: 414.187, l: "2", a: 0},
{o: 414.187, f: 422.627, l: "1", a: 1},
{o: 422.627, f: 430.68, l: "3", a: 0},
{o: 430.68, f: 448.947, l: "2", a: 1},
{o: 448.947, f: 461.08, l: "7", a: 0}],
[{o: 0, f: 42.465, l: "a", a: 0},
{o: 42.465, f: 77.48, l: "a", a: 1},
{o: 77.48, f: 385.165, l: "b", a: 0},
{o: 385.165, f: 427.63, l: "a", a: 1},
{o: 427.63, f: 461.9, l: "a", a: 0}],
[{o: 0, f: 40.925, l: "n1", a: 0},
{o: 40.925, f: 63.065, l: "A", a: 1},
{o: 63.065, f: 381.782, l: "n2", a: 0},
{o: 381.782, f: 403.516, l: "A", a: 1},
{o: 403.516, f: 416.171, l: "n3", a: 0},
{o: 416.171, f: 437.232, l: "A", a: 1},
{o: 437.232, f: 461.473, l: "n4", a: 0}],
[{o: 0, f: 0.004, l: "F", a: 0},
{o: 0.004, f: 1.276, l: "D", a: 1},
{o: 1.276, f: 21.768, l: "E", a: 0},
{o: 21.768, f: 39.228, l: "D", a: 1},
{o: 39.228, f: 55.944, l: "E", a: 0},
{o: 55.944, f: 109.012, l: "D", a: 1},
{o: 109.012, f: 130.02, l: "E", a: 0},
{o: 130.02, f: 143.204, l: "D", a: 1},
{o: 143.204, f: 160.66, l: "E", a: 0},
{o: 160.66, f: 178.12, l: "D", a: 1},
{o: 178.12, f: 194.68, l: "E", a: 0},
{o: 194.68, f: 211.388, l: "D", a: 1},
{o: 211.388, f: 228.16, l: "E", a: 0},
{o: 228.16, f: 245.356, l: "D", a: 1},
{o: 245.356, f: 262.808, l: "E", a: 0},
{o: 262.808, f: 272.696, l: "D", a: 1},
{o: 272.696, f: 296.104, l: "E", a: 0},
{o: 296.104, f: 312.132, l: "D", a: 1},
{o: 312.132, f: 329.028, l: "E", a: 0},
{o: 329.028, f: 346.372, l: "D", a: 1},
{o: 346.372, f: 365.284, l: "E", a: 0},
{o: 365.284, f: 380.144, l: "D", a: 1},
{o: 380.144, f: 397.384, l: "E", a: 0},
{o: 397.384, f: 414.22, l: "D", a: 1},
{o: 414.22, f: 431.372, l: "E", a: 0},
{o: 431.372, f: 451.86, l: "D", a: 1},
{o: 451.86, f: 456.964, l: "B", a: 0},
{o: 456.964, f: 461.611, l: "F", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001452.ogg";

var artist = "Compilations";

var track = "Omega";
