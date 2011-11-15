var data = [
[{o: 0, f: 66.598, l: "A", a: 0},
{o: 66.598, f: 75.424, l: "F", a: 1},
{o: 75.424, f: 100.267, l: "B", a: 0},
{o: 100.267, f: 131.622, l: "C", a: 1},
{o: 131.622, f: 142.774, l: "F", a: 0},
{o: 142.774, f: 197.901, l: "E", a: 1},
{o: 197.901, f: 233.935, l: "C", a: 0},
{o: 233.935, f: 355.72, l: "E", a: 1},
{o: 355.72, f: 360.176, l: "F", a: 0},
{o: 360.176, f: 384.703, l: "B", a: 1}],
[{o: 0.04, f: 16.388, l: "B", a: 0},
{o: 16.388, f: 43.504, l: "C", a: 1},
{o: 43.504, f: 59.804, l: "D", a: 0},
{o: 59.804, f: 72.976, l: "E", a: 1},
{o: 72.976, f: 101.236, l: "A", a: 0},
{o: 101.236, f: 118.3, l: "A", a: 1},
{o: 118.3, f: 134.168, l: "A", a: 0},
{o: 134.168, f: 147.324, l: "A", a: 1},
{o: 147.324, f: 160.576, l: "A", a: 0},
{o: 160.576, f: 174.392, l: "A", a: 1},
{o: 174.392, f: 192.68, l: "A", a: 0},
{o: 192.68, f: 215.436, l: "A", a: 1},
{o: 215.436, f: 232.732, l: "A", a: 0},
{o: 232.732, f: 250.468, l: "A", a: 1},
{o: 250.468, f: 267.844, l: "A", a: 0},
{o: 267.844, f: 286.924, l: "A", a: 1},
{o: 286.924, f: 302.816, l: "A", a: 0},
{o: 302.816, f: 323.42, l: "A", a: 1},
{o: 323.42, f: 331.104, l: "A", a: 0},
{o: 331.104, f: 346.356, l: "A", a: 1},
{o: 346.356, f: 358.876, l: "A", a: 0},
{o: 358.876, f: 375.032, l: "A", a: 1},
{o: 375.032, f: 384.48, l: "A", a: 0}],
[{o: 0.04, f: 16.388, l: "C", a: 0},
{o: 16.388, f: 43.504, l: "D", a: 1},
{o: 43.504, f: 59.804, l: "E", a: 0},
{o: 59.804, f: 72.976, l: "F", a: 1},
{o: 72.976, f: 101.236, l: "A", a: 0},
{o: 101.236, f: 118.3, l: "A", a: 1},
{o: 118.3, f: 134.168, l: "G", a: 0},
{o: 134.168, f: 147.324, l: "A", a: 1},
{o: 147.324, f: 160.576, l: "A", a: 0},
{o: 160.576, f: 174.392, l: "A", a: 1},
{o: 174.392, f: 192.68, l: "A", a: 0},
{o: 192.68, f: 215.436, l: "A", a: 1},
{o: 215.436, f: 232.732, l: "H", a: 0},
{o: 232.732, f: 250.468, l: "B", a: 1},
{o: 250.468, f: 267.844, l: "B", a: 0},
{o: 267.844, f: 286.924, l: "B", a: 1},
{o: 286.924, f: 302.816, l: "I", a: 0},
{o: 302.816, f: 323.42, l: "J", a: 1},
{o: 323.42, f: 331.104, l: "K", a: 0},
{o: 331.104, f: 346.356, l: "L", a: 1},
{o: 346.356, f: 358.876, l: "A", a: 0},
{o: 358.876, f: 375.032, l: "A", a: 1},
{o: 375.032, f: 384.48, l: "A", a: 0}],
[{o: 0.76, f: 43.227, l: "4", a: 0},
{o: 43.227, f: 59.453, l: "6", a: 1},
{o: 59.453, f: 65.613, l: "8", a: 0},
{o: 65.613, f: 72.707, l: "7", a: 1},
{o: 72.707, f: 99.307, l: "2", a: 0},
{o: 99.307, f: 118.053, l: "1", a: 1},
{o: 118.053, f: 127.4, l: "5", a: 0},
{o: 127.4, f: 141.387, l: "2", a: 1},
{o: 141.387, f: 196.373, l: "3", a: 0},
{o: 196.373, f: 215.813, l: "1", a: 1},
{o: 215.813, f: 224.747, l: "5", a: 0},
{o: 224.747, f: 233.533, l: "2", a: 1},
{o: 233.533, f: 354.253, l: "1", a: 0},
{o: 354.253, f: 384.467, l: "2", a: 1}],
[{o: 0, f: 70.03, l: "a", a: 0},
{o: 70.03, f: 140.06, l: "b", a: 1},
{o: 140.06, f: 165.39, l: "c", a: 0},
{o: 165.39, f: 237.655, l: "b", a: 1},
{o: 237.655, f: 270.435, l: "d", a: 0},
{o: 270.435, f: 335.995, l: "b", a: 1},
{o: 335.995, f: 384.42, l: "e", a: 0}],
[{o: 0, f: 77.288, l: "n1", a: 0},
{o: 77.288, f: 86.285, l: "A", a: 1},
{o: 86.285, f: 95.364, l: "A", a: 0},
{o: 95.364, f: 100.415, l: "n2", a: 1},
{o: 100.415, f: 109.47, l: "B", a: 0},
{o: 109.47, f: 118.608, l: "B", a: 1},
{o: 118.608, f: 198.078, l: "n3", a: 0},
{o: 198.078, f: 207.168, l: "B", a: 1},
{o: 207.168, f: 216.329, l: "B", a: 0},
{o: 216.329, f: 234.928, l: "n4", a: 1},
{o: 234.928, f: 249.951, l: "D", a: 0},
{o: 249.951, f: 265.16, l: "D", a: 1},
{o: 265.16, f: 285.861, l: "n5", a: 0},
{o: 285.861, f: 303.38, l: "C", a: 1},
{o: 303.38, f: 320.702, l: "C", a: 0},
{o: 320.702, f: 362.069, l: "n6", a: 1},
{o: 362.069, f: 370.985, l: "A", a: 0},
{o: 370.985, f: 379.925, l: "A", a: 1},
{o: 379.925, f: 384.569, l: "n7", a: 0}],
[{o: 0, f: 0.004, l: "I", a: 0},
{o: 0.004, f: 0.004, l: "B", a: 1},
{o: 0.004, f: 48.668, l: "A", a: 0},
{o: 48.668, f: 57.656, l: "D", a: 1},
{o: 57.656, f: 60.74, l: "A", a: 0},
{o: 60.74, f: 64.464, l: "D", a: 1},
{o: 64.464, f: 72.828, l: "A", a: 0},
{o: 72.828, f: 122.752, l: "D", a: 1},
{o: 122.752, f: 135.704, l: "H", a: 0},
{o: 135.704, f: 220.22, l: "D", a: 1},
{o: 220.22, f: 222.832, l: "H", a: 0},
{o: 222.832, f: 225.556, l: "D", a: 1},
{o: 225.556, f: 235.544, l: "H", a: 0},
{o: 235.544, f: 335.24, l: "D", a: 1},
{o: 335.24, f: 356.24, l: "H", a: 0},
{o: 356.24, f: 384.556, l: "D", a: 1},
{o: 384.556, f: 384.6, l: "I", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001207.ogg";

var artist = "On the One";

var track = "Schlickside";
