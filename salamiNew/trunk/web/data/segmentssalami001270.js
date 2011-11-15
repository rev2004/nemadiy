var data = [
[{o: 0, f: 1.288, l: "Z", a: 0},
{o: 1.288, f: 13.016, l: "A", a: 1},
{o: 13.016, f: 36.008, l: "A", a: 0},
{o: 36.008, f: 58.96, l: "B", a: 1},
{o: 58.96, f: 70.528, l: "A", a: 0},
{o: 70.528, f: 93.624, l: "A", a: 1},
{o: 93.624, f: 116.807, l: "B", a: 0},
{o: 116.807, f: 125.646, l: "C", a: 1},
{o: 125.646, f: 170.677, l: "A", a: 0},
{o: 170.677, f: 193.573, l: "A", a: 1},
{o: 193.573, f: 216.466, l: "B", a: 0},
{o: 216.466, f: 225.232, l: "C", a: 1},
{o: 225.232, f: 284.59, l: "D", a: 0},
{o: 284.59, f: 372.574, l: "D", a: 1},
{o: 372.574, f: 384.297, l: "F", a: 0},
{o: 384.297, f: 419.317, l: "D", a: 1},
{o: 419.317, f: 433.412, l: "C", a: 0},
{o: 433.412, f: 457.274, l: "Z", a: 1}],
[{o: 0.004, f: 12.64, l: "C", a: 0},
{o: 12.64, f: 35.228, l: "C", a: 1},
{o: 35.228, f: 58.208, l: "C", a: 0},
{o: 58.208, f: 70.116, l: "C", a: 1},
{o: 70.116, f: 91.144, l: "C", a: 0},
{o: 91.144, f: 109.488, l: "C", a: 1},
{o: 109.488, f: 118.892, l: "C", a: 0},
{o: 118.892, f: 130.496, l: "C", a: 1},
{o: 130.496, f: 143.604, l: "C", a: 0},
{o: 143.604, f: 160.124, l: "C", a: 1},
{o: 160.124, f: 170.3, l: "C", a: 0},
{o: 170.3, f: 193.536, l: "C", a: 1},
{o: 193.536, f: 205.3, l: "C", a: 0},
{o: 205.3, f: 225.536, l: "C", a: 1},
{o: 225.536, f: 242.688, l: "C", a: 0},
{o: 242.688, f: 260.98, l: "C", a: 1},
{o: 260.98, f: 283.12, l: "A", a: 0},
{o: 283.12, f: 297.04, l: "C", a: 1},
{o: 297.04, f: 319.244, l: "B", a: 0},
{o: 319.244, f: 329.404, l: "B", a: 1},
{o: 329.404, f: 346.684, l: "B", a: 0},
{o: 346.684, f: 354.524, l: "A", a: 1},
{o: 354.524, f: 375.032, l: "B", a: 0},
{o: 375.032, f: 384.2, l: "B", a: 1},
{o: 384.2, f: 395.156, l: "B", a: 0},
{o: 395.156, f: 419.288, l: "B", a: 1},
{o: 419.288, f: 430.452, l: "B", a: 0},
{o: 430.452, f: 441.792, l: "B", a: 1},
{o: 441.792, f: 455.908, l: "D", a: 0}],
[{o: 0.004, f: 12.64, l: "D", a: 0},
{o: 12.64, f: 35.228, l: "A", a: 1},
{o: 35.228, f: 58.208, l: "A", a: 0},
{o: 58.208, f: 70.116, l: "A", a: 1},
{o: 70.116, f: 91.144, l: "A", a: 0},
{o: 91.144, f: 109.488, l: "A", a: 1},
{o: 109.488, f: 118.892, l: "A", a: 0},
{o: 118.892, f: 130.496, l: "A", a: 1},
{o: 130.496, f: 143.604, l: "A", a: 0},
{o: 143.604, f: 160.124, l: "C", a: 1},
{o: 160.124, f: 170.3, l: "C", a: 0},
{o: 170.3, f: 193.536, l: "A", a: 1},
{o: 193.536, f: 205.3, l: "C", a: 0},
{o: 205.3, f: 225.536, l: "C", a: 1},
{o: 225.536, f: 242.688, l: "C", a: 0},
{o: 242.688, f: 260.98, l: "C", a: 1},
{o: 260.98, f: 283.12, l: "E", a: 0},
{o: 283.12, f: 297.04, l: "A", a: 1},
{o: 297.04, f: 319.244, l: "F", a: 0},
{o: 319.244, f: 329.404, l: "G", a: 1},
{o: 329.404, f: 346.684, l: "B", a: 0},
{o: 346.684, f: 354.524, l: "H", a: 1},
{o: 354.524, f: 375.032, l: "B", a: 0},
{o: 375.032, f: 384.2, l: "I", a: 1},
{o: 384.2, f: 395.156, l: "J", a: 0},
{o: 395.156, f: 419.288, l: "K", a: 1},
{o: 419.288, f: 430.452, l: "L", a: 0},
{o: 430.452, f: 441.792, l: "M", a: 1},
{o: 441.792, f: 455.908, l: "N", a: 0}],
[{o: 0.573, f: 2.2, l: "5", a: 0},
{o: 2.2, f: 35.747, l: "1", a: 1},
{o: 35.747, f: 44.293, l: "2", a: 0},
{o: 44.293, f: 58.733, l: "1", a: 1},
{o: 58.733, f: 69.933, l: "2", a: 0},
{o: 69.933, f: 85.893, l: "1", a: 1},
{o: 85.893, f: 104.2, l: "2", a: 0},
{o: 104.2, f: 116.893, l: "1", a: 1},
{o: 116.893, f: 125.64, l: "2", a: 0},
{o: 125.64, f: 136.32, l: "3", a: 1},
{o: 136.32, f: 144.107, l: "7", a: 0},
{o: 144.107, f: 156.813, l: "3", a: 1},
{o: 156.813, f: 170.48, l: "6", a: 0},
{o: 170.48, f: 193.347, l: "1", a: 1},
{o: 193.347, f: 204.04, l: "2", a: 0},
{o: 204.04, f: 216.253, l: "1", a: 1},
{o: 216.253, f: 225.013, l: "2", a: 0},
{o: 225.013, f: 235.48, l: "1", a: 1},
{o: 235.48, f: 252.24, l: "3", a: 0},
{o: 252.24, f: 260.467, l: "1", a: 1},
{o: 260.467, f: 283.653, l: "4", a: 0},
{o: 283.653, f: 304.893, l: "1", a: 1},
{o: 304.893, f: 312.173, l: "2", a: 0},
{o: 312.173, f: 318.693, l: "1", a: 1},
{o: 318.693, f: 323.773, l: "2", a: 0},
{o: 323.773, f: 330.667, l: "1", a: 1},
{o: 330.667, f: 336.173, l: "3", a: 0},
{o: 336.173, f: 372.28, l: "2", a: 1},
{o: 372.28, f: 384.413, l: "1", a: 0},
{o: 384.413, f: 388.8, l: "3", a: 1},
{o: 388.8, f: 394.613, l: "1", a: 0},
{o: 394.613, f: 400.093, l: "4", a: 1},
{o: 400.093, f: 411.427, l: "3", a: 0},
{o: 411.427, f: 419.453, l: "1", a: 1},
{o: 419.453, f: 429.173, l: "2", a: 0},
{o: 429.173, f: 435.293, l: "8", a: 1},
{o: 435.293, f: 455.587, l: "5", a: 0}],
[{o: 0, f: 20.115, l: "a", a: 0},
{o: 20.115, f: 66.305, l: "b", a: 1},
{o: 66.305, f: 123.67, l: "b", a: 0},
{o: 123.67, f: 178.8, l: "c", a: 1},
{o: 178.8, f: 224.245, l: "b", a: 0},
{o: 224.245, f: 457.43, l: "d", a: 1}],
[{o: 0, f: 15.139, l: "n1", a: 0},
{o: 15.139, f: 28.061, l: "A", a: 1},
{o: 28.061, f: 55.356, l: "n2", a: 0},
{o: 55.356, f: 68.36, l: "A", a: 1},
{o: 68.36, f: 84.3, l: "n3", a: 0},
{o: 84.3, f: 98.685, l: "A", a: 1},
{o: 98.685, f: 111.723, l: "A", a: 0},
{o: 111.723, f: 127.663, l: "C", a: 1},
{o: 127.663, f: 192.853, l: "n5", a: 0},
{o: 192.853, f: 205.694, l: "A", a: 1},
{o: 205.694, f: 211.453, l: "n6", a: 0},
{o: 211.453, f: 227.451, l: "C", a: 1},
{o: 227.451, f: 267.25, l: "n7", a: 0},
{o: 267.25, f: 279.069, l: "B", a: 1},
{o: 279.069, f: 284.955, l: "n8", a: 0},
{o: 284.955, f: 296.693, l: "B", a: 1},
{o: 296.693, f: 308.361, l: "B", a: 0},
{o: 308.361, f: 322.885, l: "n9", a: 1},
{o: 322.885, f: 334.53, l: "B", a: 0},
{o: 334.53, f: 457.015, l: "n10", a: 1}],
[{o: 0, f: 0.052, l: "G", a: 0},
{o: 0.052, f: 6.988, l: "B", a: 1},
{o: 6.988, f: 19.924, l: "E", a: 0},
{o: 19.924, f: 30.028, l: "A", a: 1},
{o: 30.028, f: 42.88, l: "E", a: 0},
{o: 42.88, f: 92.956, l: "A", a: 1},
{o: 92.956, f: 147.972, l: "E", a: 0},
{o: 147.972, f: 171.384, l: "F", a: 1},
{o: 171.384, f: 193.372, l: "A", a: 0},
{o: 193.372, f: 203.34, l: "E", a: 1},
{o: 203.34, f: 216.088, l: "A", a: 0},
{o: 216.088, f: 237.864, l: "E", a: 1},
{o: 237.864, f: 258.572, l: "F", a: 0},
{o: 258.572, f: 389.68, l: "A", a: 1},
{o: 389.68, f: 396.968, l: "C", a: 0},
{o: 396.968, f: 418.552, l: "A", a: 1},
{o: 418.552, f: 427.352, l: "E", a: 0},
{o: 427.352, f: 457.12, l: "B", a: 1},
{o: 457.12, f: 457.187, l: "G", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001270.ogg";

var artist = "Guta";

var track = "The Moon";
