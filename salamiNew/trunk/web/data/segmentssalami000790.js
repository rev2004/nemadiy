var data = [
[{o: 0.731, f: 15.876, l: "A", a: 0},
{o: 15.876, f: 92.055, l: "B", a: 1},
{o: 92.055, f: 108.332, l: "C", a: 0},
{o: 108.332, f: 142.976, l: "D", a: 1},
{o: 142.976, f: 203.452, l: "E", a: 0},
{o: 203.452, f: 258.518, l: "F", a: 1},
{o: 258.518, f: 333.492, l: "G", a: 0},
{o: 333.492, f: 415.226, l: "H", a: 1},
{o: 415.226, f: 616.046, l: "I", a: 0}],
[{o: 0, f: 616.047, l: "A", a: 0}],
[{o: 0, f: 616.047, l: "A", a: 0}],
[{o: 0.44, f: 1.613, l: "3", a: 0},
{o: 1.613, f: 10.12, l: "2", a: 1},
{o: 10.12, f: 48.693, l: "1", a: 0},
{o: 48.693, f: 59.213, l: "2", a: 1},
{o: 59.213, f: 72.067, l: "1", a: 0},
{o: 72.067, f: 78.24, l: "5", a: 1},
{o: 78.24, f: 97.213, l: "1", a: 0},
{o: 97.213, f: 102.253, l: "5", a: 1},
{o: 102.253, f: 108.6, l: "1", a: 0},
{o: 108.6, f: 114.493, l: "5", a: 1},
{o: 114.493, f: 199.24, l: "1", a: 0},
{o: 199.24, f: 213.48, l: "4", a: 1},
{o: 213.48, f: 229.547, l: "1", a: 0},
{o: 229.547, f: 240.707, l: "4", a: 1},
{o: 240.707, f: 258.667, l: "2", a: 0},
{o: 258.667, f: 265.307, l: "4", a: 1},
{o: 265.307, f: 270.893, l: "5", a: 0},
{o: 270.893, f: 275.973, l: "1", a: 1},
{o: 275.973, f: 295.213, l: "2", a: 0},
{o: 295.213, f: 304.96, l: "5", a: 1},
{o: 304.96, f: 311.093, l: "2", a: 0},
{o: 311.093, f: 323.96, l: "5", a: 1},
{o: 323.96, f: 409.947, l: "1", a: 0},
{o: 409.947, f: 415.56, l: "6", a: 1},
{o: 415.56, f: 432.347, l: "4", a: 0},
{o: 432.347, f: 436.867, l: "2", a: 1},
{o: 436.867, f: 441.04, l: "4", a: 0},
{o: 441.04, f: 447.04, l: "2", a: 1},
{o: 447.04, f: 451.947, l: "8", a: 0},
{o: 451.947, f: 478.52, l: "2", a: 1},
{o: 478.52, f: 496.547, l: "3", a: 0},
{o: 496.547, f: 507.947, l: "4", a: 1},
{o: 507.947, f: 513.653, l: "2", a: 0},
{o: 513.653, f: 567.907, l: "3", a: 1},
{o: 567.907, f: 579.64, l: "2", a: 0},
{o: 579.64, f: 604.933, l: "3", a: 1},
{o: 604.933, f: 610.947, l: "6", a: 0},
{o: 610.947, f: 615.96, l: "7", a: 1}],
[{o: 0, f: 461.9, l: "a", a: 0},
{o: 461.9, f: 498.405, l: "b", a: 1},
{o: 498.405, f: 581.1, l: "c", a: 0},
{o: 581.1, f: 613.135, l: "b", a: 1},
{o: 613.135, f: 616.86, l: "d", a: 0}],
[{o: 0, f: 206.472, l: "n1", a: 0},
{o: 206.472, f: 220.694, l: "A", a: 1},
{o: 220.694, f: 332.591, l: "n2", a: 0},
{o: 332.591, f: 343.307, l: "B", a: 1},
{o: 343.307, f: 354.093, l: "B", a: 0},
{o: 354.093, f: 390.629, l: "n3", a: 1},
{o: 390.629, f: 400.904, l: "A", a: 0},
{o: 400.904, f: 539.899, l: "n4", a: 1},
{o: 539.899, f: 553.285, l: "C", a: 0},
{o: 553.285, f: 564.384, l: "C", a: 1},
{o: 564.384, f: 616.025, l: "n6", a: 0}],
[{o: 0, f: 0.016, l: "J", a: 0},
{o: 0.016, f: 7.72, l: "H", a: 1},
{o: 7.72, f: 41.764, l: "A", a: 0},
{o: 41.764, f: 63.932, l: "H", a: 1},
{o: 63.932, f: 83.532, l: "A", a: 0},
{o: 83.532, f: 91.92, l: "I", a: 1},
{o: 91.92, f: 106.888, l: "G", a: 0},
{o: 106.888, f: 113.932, l: "A", a: 1},
{o: 113.932, f: 122.188, l: "B", a: 0},
{o: 122.188, f: 142.032, l: "I", a: 1},
{o: 142.032, f: 149.716, l: "G", a: 0},
{o: 149.716, f: 240.676, l: "I", a: 1},
{o: 240.676, f: 253.764, l: "C", a: 0},
{o: 253.764, f: 293.028, l: "H", a: 1},
{o: 293.028, f: 355.176, l: "A", a: 0},
{o: 355.176, f: 435.164, l: "H", a: 1},
{o: 435.164, f: 447.764, l: "I", a: 0},
{o: 447.764, f: 459.072, l: "A", a: 1},
{o: 459.072, f: 559.504, l: "H", a: 0},
{o: 559.504, f: 568.248, l: "E", a: 1},
{o: 568.248, f: 579.992, l: "A", a: 0},
{o: 579.992, f: 605.292, l: "H", a: 1},
{o: 605.292, f: 606.616, l: "A", a: 0},
{o: 606.616, f: 616.047, l: "J", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000790.ogg";

var artist = "Maurizio Pollini";

var track = "Piano Sonata in B minor S";
