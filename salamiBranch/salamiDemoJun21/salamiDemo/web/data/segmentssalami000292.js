var data = [
[{o: 2.265, f: 28.539, l: "A", a: 0},
{o: 28.539, f: 56.751, l: "B", a: 1},
{o: 56.751, f: 131.598, l: "C", a: 0},
{o: 131.598, f: 257.567, l: "D", a: 1},
{o: 257.567, f: 284.408, l: "B''", a: 0},
{o: 284.408, f: 406.457, l: "E", a: 1},
{o: 406.457, f: 432.556, l: "B", a: 0},
{o: 432.556, f: 556.961, l: "G", a: 1},
{o: 556.961, f: 614.346, l: "B", a: 0}],
[{o: 0, f: 614.323, l: "A", a: 0}],
[{o: 0, f: 614.323, l: "A", a: 0}],
[{o: 0.413, f: 1.84, l: "8", a: 0},
{o: 1.84, f: 21.88, l: "5", a: 1},
{o: 21.88, f: 45.933, l: "2", a: 0},
{o: 45.933, f: 51.6, l: "1", a: 1},
{o: 51.6, f: 61.04, l: "2", a: 0},
{o: 61.04, f: 67.32, l: "3", a: 1},
{o: 67.32, f: 75.893, l: "2", a: 0},
{o: 75.893, f: 109.147, l: "3", a: 1},
{o: 109.147, f: 119.147, l: "4", a: 0},
{o: 119.147, f: 132.267, l: "2", a: 1},
{o: 132.267, f: 140.133, l: "4", a: 0},
{o: 140.133, f: 152.747, l: "6", a: 1},
{o: 152.747, f: 165.173, l: "4", a: 0},
{o: 165.173, f: 175.333, l: "1", a: 1},
{o: 175.333, f: 185.08, l: "3", a: 0},
{o: 185.08, f: 191.293, l: "6", a: 1},
{o: 191.293, f: 199.68, l: "2", a: 0},
{o: 199.68, f: 205.92, l: "1", a: 1},
{o: 205.92, f: 224.053, l: "2", a: 0},
{o: 224.053, f: 233.493, l: "3", a: 1},
{o: 233.493, f: 284.627, l: "1", a: 0},
{o: 284.627, f: 302.587, l: "2", a: 1},
{o: 302.587, f: 324.747, l: "1", a: 0},
{o: 324.747, f: 335.173, l: "4", a: 1},
{o: 335.173, f: 353.12, l: "2", a: 0},
{o: 353.12, f: 362.293, l: "7", a: 1},
{o: 362.293, f: 373.093, l: "3", a: 0},
{o: 373.093, f: 384.653, l: "1", a: 1},
{o: 384.653, f: 395.947, l: "4", a: 0},
{o: 395.947, f: 403.52, l: "7", a: 1},
{o: 403.52, f: 445.307, l: "1", a: 0},
{o: 445.307, f: 455.133, l: "4", a: 1},
{o: 455.133, f: 462.107, l: "2", a: 0},
{o: 462.107, f: 482.32, l: "1", a: 1},
{o: 482.32, f: 491.427, l: "3", a: 0},
{o: 491.427, f: 511.28, l: "1", a: 1},
{o: 511.28, f: 515.8, l: "2", a: 0},
{o: 515.8, f: 584.8, l: "1", a: 1},
{o: 584.8, f: 592.507, l: "3", a: 0},
{o: 592.507, f: 605.893, l: "1", a: 1},
{o: 605.893, f: 609.667, l: "5", a: 0}],
[{o: 0, f: 0.745, l: "a", a: 0},
{o: 0.745, f: 56.62, l: "b", a: 1},
{o: 56.62, f: 207.11, l: "c", a: 0},
{o: 207.11, f: 246.595, l: "b", a: 1},
{o: 246.595, f: 541.615, l: "d", a: 0},
{o: 541.615, f: 612.39, l: "b", a: 1},
{o: 612.39, f: 614.625, l: "a", a: 0}],
[{o: 0, f: 22.883, l: "n1", a: 0},
{o: 22.883, f: 36.757, l: "A", a: 1},
{o: 36.757, f: 49.226, l: "A", a: 0},
{o: 49.226, f: 60.534, l: "A", a: 1},
{o: 60.534, f: 78.82, l: "n4", a: 0},
{o: 78.82, f: 90.906, l: "B", a: 1},
{o: 90.906, f: 99.567, l: "n5", a: 0},
{o: 99.567, f: 112.652, l: "B", a: 1},
{o: 112.652, f: 205.172, l: "n6", a: 0},
{o: 205.172, f: 214.076, l: "A", a: 1},
{o: 214.076, f: 234.011, l: "n7", a: 0},
{o: 234.011, f: 246.584, l: "B", a: 1},
{o: 246.584, f: 285.129, l: "n8", a: 0},
{o: 285.129, f: 297.958, l: "C", a: 1},
{o: 297.958, f: 313.992, l: "n9", a: 0},
{o: 313.992, f: 327.065, l: "C", a: 1},
{o: 327.065, f: 422.777, l: "n10", a: 0},
{o: 422.777, f: 435.212, l: "B", a: 1},
{o: 435.212, f: 492.53, l: "n11", a: 0},
{o: 492.53, f: 504.57, l: "B", a: 1},
{o: 504.57, f: 512.499, l: "n12", a: 0},
{o: 512.499, f: 524.643, l: "D", a: 1},
{o: 524.643, f: 535.324, l: "A", a: 0},
{o: 535.324, f: 549.28, l: "n14", a: 1},
{o: 549.28, f: 564.465, l: "A", a: 0},
{o: 564.465, f: 575.808, l: "A", a: 1},
{o: 575.808, f: 598.529, l: "n16", a: 0},
{o: 598.529, f: 606.923, l: "D", a: 1},
{o: 606.923, f: 614.168, l: "n17", a: 0}],
[{o: 0, f: 0.976, l: "L", a: 0},
{o: 0.976, f: 5.22, l: "C", a: 1},
{o: 5.22, f: 25.524, l: "J", a: 0},
{o: 25.524, f: 120.852, l: "B", a: 1},
{o: 120.852, f: 158.268, l: "J", a: 0},
{o: 158.268, f: 175.836, l: "K", a: 1},
{o: 175.836, f: 194.072, l: "J", a: 0},
{o: 194.072, f: 214.684, l: "B", a: 1},
{o: 214.684, f: 219.98, l: "J", a: 0},
{o: 219.98, f: 229.084, l: "B", a: 1},
{o: 229.084, f: 236.188, l: "I", a: 0},
{o: 236.188, f: 241.88, l: "J", a: 1},
{o: 241.88, f: 275.176, l: "B", a: 0},
{o: 275.176, f: 280.22, l: "D", a: 1},
{o: 280.22, f: 309.528, l: "G", a: 0},
{o: 309.528, f: 344.804, l: "B", a: 1},
{o: 344.804, f: 348.636, l: "I", a: 0},
{o: 348.636, f: 403.612, l: "J", a: 1},
{o: 403.612, f: 415.956, l: "K", a: 0},
{o: 415.956, f: 419.916, l: "B", a: 1},
{o: 419.916, f: 446.044, l: "C", a: 0},
{o: 446.044, f: 450.82, l: "J", a: 1},
{o: 450.82, f: 476.068, l: "B", a: 0},
{o: 476.068, f: 482.196, l: "J", a: 1},
{o: 482.196, f: 504.44, l: "B", a: 0},
{o: 504.44, f: 511.76, l: "J", a: 1},
{o: 511.76, f: 523.216, l: "B", a: 0},
{o: 523.216, f: 527.792, l: "C", a: 1},
{o: 527.792, f: 570.808, l: "B", a: 0},
{o: 570.808, f: 582.996, l: "C", a: 1},
{o: 582.996, f: 606.004, l: "B", a: 0},
{o: 606.004, f: 609.76, l: "C", a: 1},
{o: 609.76, f: 614.323, l: "L", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000292.ogg";

var artist = "Sarah Chang";

var track = "Chaconne";
