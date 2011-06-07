var data = [
[{o: 0, f: 14.978, l: "I", a: 0},
{o: 14.978, f: 45.706, l: "A", a: 1},
{o: 45.706, f: 53.903, l: "B", a: 0},
{o: 53.903, f: 78.066, l: "A", a: 1},
{o: 78.066, f: 86.079, l: "B", a: 0},
{o: 86.079, f: 110.238, l: "A", a: 1},
{o: 110.238, f: 118.942, l: "B", a: 0},
{o: 118.942, f: 134.814, l: "Z", a: 1},
{o: 134.814, f: 159.409, l: "C", a: 0},
{o: 159.409, f: 190.451, l: "A", a: 1},
{o: 190.451, f: 202.59, l: "B", a: 0},
{o: 202.59, f: 209.558, l: "Z", a: 1}],
[{o: 0.004, f: 8.296, l: "D", a: 0},
{o: 8.296, f: 22.036, l: "B", a: 1},
{o: 22.036, f: 31.768, l: "A", a: 0},
{o: 31.768, f: 48.72, l: "A", a: 1},
{o: 48.72, f: 57.872, l: "C", a: 0},
{o: 57.872, f: 75.76, l: "C", a: 1},
{o: 75.76, f: 85.012, l: "C", a: 0},
{o: 85.012, f: 104.608, l: "A", a: 1},
{o: 104.608, f: 119.056, l: "A", a: 0},
{o: 119.056, f: 133.744, l: "D", a: 1},
{o: 133.744, f: 151.04, l: "A", a: 0},
{o: 151.04, f: 164.244, l: "D", a: 1},
{o: 164.244, f: 176.256, l: "D", a: 0},
{o: 176.256, f: 192.324, l: "D", a: 1},
{o: 192.324, f: 200.692, l: "E", a: 0},
{o: 200.692, f: 209.444, l: "D", a: 1}],
[{o: 0.004, f: 8.296, l: "D", a: 0},
{o: 8.296, f: 22.036, l: "E", a: 1},
{o: 22.036, f: 31.768, l: "A", a: 0},
{o: 31.768, f: 48.72, l: "A", a: 1},
{o: 48.72, f: 57.872, l: "C", a: 0},
{o: 57.872, f: 75.76, l: "C", a: 1},
{o: 75.76, f: 85.012, l: "C", a: 0},
{o: 85.012, f: 104.608, l: "F", a: 1},
{o: 104.608, f: 119.056, l: "A", a: 0},
{o: 119.056, f: 133.744, l: "G", a: 1},
{o: 133.744, f: 151.04, l: "A", a: 0},
{o: 151.04, f: 164.244, l: "B", a: 1},
{o: 164.244, f: 176.256, l: "B", a: 0},
{o: 176.256, f: 192.324, l: "B", a: 1},
{o: 192.324, f: 200.692, l: "H", a: 0},
{o: 200.692, f: 209.444, l: "I", a: 1}],
[{o: 0.573, f: 3.36, l: "2", a: 0},
{o: 3.36, f: 16.133, l: "6", a: 1},
{o: 16.133, f: 53.653, l: "4", a: 0},
{o: 53.653, f: 101.84, l: "1", a: 1},
{o: 101.84, f: 109.253, l: "3", a: 0},
{o: 109.253, f: 120.627, l: "1", a: 1},
{o: 120.627, f: 128.907, l: "2", a: 0},
{o: 128.907, f: 134.533, l: "8", a: 1},
{o: 134.533, f: 155.653, l: "5", a: 0},
{o: 155.653, f: 168.987, l: "3", a: 1},
{o: 168.987, f: 190.08, l: "1", a: 0},
{o: 190.08, f: 201.68, l: "7", a: 1},
{o: 201.68, f: 209.4, l: "2", a: 0}],
[{o: 0, f: 16.39, l: "a", a: 0},
{o: 16.39, f: 29.055, l: "b", a: 1},
{o: 29.055, f: 51.405, l: "c", a: 0},
{o: 51.405, f: 65.56, l: "b", a: 1},
{o: 65.56, f: 187.74, l: "d", a: 0},
{o: 187.74, f: 198.915, l: "b", a: 1},
{o: 198.915, f: 208.6, l: "e", a: 0}],
[{o: 0, f: 209.096, l: "A", a: 0},
{o: 0.035, f: 209.212, l: "A", a: 1}],
[{o: 0, f: 0.004, l: "G", a: 0},
{o: 0.004, f: 8.296, l: "B", a: 1},
{o: 8.296, f: 58.404, l: "E", a: 0},
{o: 58.404, f: 80.712, l: "F", a: 1},
{o: 80.712, f: 100.368, l: "B", a: 0},
{o: 100.368, f: 134.464, l: "F", a: 1},
{o: 134.464, f: 160.048, l: "A", a: 0},
{o: 160.048, f: 194.036, l: "F", a: 1},
{o: 194.036, f: 209.444, l: "C", a: 0},
{o: 209.444, f: 209.521, l: "G", a: 1}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami001324.ogg";
