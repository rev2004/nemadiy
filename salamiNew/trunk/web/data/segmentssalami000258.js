var data = [
[{o: 0.352, f: 9.379, l: "A", a: 0},
{o: 9.379, f: 35.834, l: "B", a: 1},
{o: 35.834, f: 40.254, l: "A", a: 0},
{o: 40.254, f: 100.889, l: "B", a: 1},
{o: 100.889, f: 111.016, l: "C", a: 0},
{o: 111.016, f: 116.222, l: "A", a: 1},
{o: 116.222, f: 180.842, l: "B", a: 0},
{o: 180.842, f: 290.048, l: "D", a: 1},
{o: 290.048, f: 305.64, l: "E", a: 0},
{o: 305.64, f: 309.946, l: "A", a: 1},
{o: 309.946, f: 342.125, l: "B", a: 0},
{o: 342.125, f: 353.468, l: "C", a: 1},
{o: 353.468, f: 380.197, l: "F", a: 0},
{o: 380.197, f: 395.028, l: "E", a: 1},
{o: 395.028, f: 419.348, l: "F", a: 0}],
[{o: 0.224, f: 17.524, l: "D", a: 0},
{o: 17.524, f: 34.688, l: "D", a: 1},
{o: 34.688, f: 45.404, l: "D", a: 0},
{o: 45.404, f: 56.38, l: "D", a: 1},
{o: 56.38, f: 71.296, l: "D", a: 0},
{o: 71.296, f: 91.08, l: "D", a: 1},
{o: 91.08, f: 102.936, l: "D", a: 0},
{o: 102.936, f: 126.884, l: "D", a: 1},
{o: 126.884, f: 148.056, l: "D", a: 0},
{o: 148.056, f: 170.344, l: "D", a: 1},
{o: 170.344, f: 179.832, l: "D", a: 0},
{o: 179.832, f: 191.216, l: "D", a: 1},
{o: 191.216, f: 203.82, l: "A", a: 0},
{o: 203.82, f: 221.904, l: "D", a: 1},
{o: 221.904, f: 236.12, l: "D", a: 0},
{o: 236.12, f: 249.256, l: "D", a: 1},
{o: 249.256, f: 255.768, l: "D", a: 0},
{o: 255.768, f: 268.46, l: "D", a: 1},
{o: 268.46, f: 279.412, l: "D", a: 0},
{o: 279.412, f: 298.608, l: "D", a: 1},
{o: 298.608, f: 316.124, l: "D", a: 0},
{o: 316.124, f: 324.92, l: "D", a: 1},
{o: 324.92, f: 340.908, l: "D", a: 0},
{o: 340.908, f: 348.58, l: "D", a: 1},
{o: 348.58, f: 360.872, l: "D", a: 0},
{o: 360.872, f: 383.012, l: "D", a: 1},
{o: 383.012, f: 402.652, l: "D", a: 0},
{o: 402.652, f: 414.212, l: "D", a: 1}],
[{o: 0.224, f: 17.524, l: "D", a: 0},
{o: 17.524, f: 34.688, l: "F", a: 1},
{o: 34.688, f: 45.404, l: "G", a: 0},
{o: 45.404, f: 56.38, l: "F", a: 1},
{o: 56.38, f: 71.296, l: "F", a: 0},
{o: 71.296, f: 91.08, l: "F", a: 1},
{o: 91.08, f: 102.936, l: "F", a: 0},
{o: 102.936, f: 126.884, l: "F", a: 1},
{o: 126.884, f: 148.056, l: "F", a: 0},
{o: 148.056, f: 170.344, l: "F", a: 1},
{o: 170.344, f: 179.832, l: "H", a: 0},
{o: 179.832, f: 191.216, l: "I", a: 1},
{o: 191.216, f: 203.82, l: "J", a: 0},
{o: 203.82, f: 221.904, l: "B", a: 1},
{o: 221.904, f: 236.12, l: "B", a: 0},
{o: 236.12, f: 249.256, l: "A", a: 1},
{o: 249.256, f: 255.768, l: "K", a: 0},
{o: 255.768, f: 268.46, l: "L", a: 1},
{o: 268.46, f: 279.412, l: "A", a: 0},
{o: 279.412, f: 298.608, l: "C", a: 1},
{o: 298.608, f: 316.124, l: "F", a: 0},
{o: 316.124, f: 324.92, l: "F", a: 1},
{o: 324.92, f: 340.908, l: "C", a: 0},
{o: 340.908, f: 348.58, l: "M", a: 1},
{o: 348.58, f: 360.872, l: "E", a: 0},
{o: 360.872, f: 383.012, l: "F", a: 1},
{o: 383.012, f: 402.652, l: "F", a: 0},
{o: 402.652, f: 414.212, l: "E", a: 1}],
[{o: 0.68, f: 7.8, l: "2", a: 0},
{o: 7.8, f: 29.987, l: "4", a: 1},
{o: 29.987, f: 41.707, l: "2", a: 0},
{o: 41.707, f: 54.133, l: "1", a: 1},
{o: 54.133, f: 71.307, l: "3", a: 0},
{o: 71.307, f: 80.187, l: "1", a: 1},
{o: 80.187, f: 92.533, l: "4", a: 0},
{o: 92.533, f: 99.2, l: "3", a: 1},
{o: 99.2, f: 106.12, l: "2", a: 0},
{o: 106.12, f: 161.653, l: "1", a: 1},
{o: 161.653, f: 180.48, l: "3", a: 0},
{o: 180.48, f: 190.2, l: "8", a: 1},
{o: 190.2, f: 209.76, l: "6", a: 0},
{o: 209.76, f: 215.813, l: "7", a: 1},
{o: 215.813, f: 222.707, l: "6", a: 0},
{o: 222.707, f: 239.253, l: "7", a: 1},
{o: 239.253, f: 248.653, l: "5", a: 0},
{o: 248.653, f: 263.413, l: "4", a: 1},
{o: 263.413, f: 283.627, l: "5", a: 0},
{o: 283.627, f: 303.52, l: "1", a: 1},
{o: 303.52, f: 311.093, l: "2", a: 0},
{o: 311.093, f: 325.2, l: "1", a: 1},
{o: 325.2, f: 342.08, l: "3", a: 0},
{o: 342.08, f: 351.413, l: "2", a: 1},
{o: 351.413, f: 411.427, l: "1", a: 0},
{o: 411.427, f: 413.587, l: "2", a: 1}],
[{o: 0, f: 26.075, l: "a", a: 0},
{o: 26.075, f: 84.93, l: "b", a: 1},
{o: 84.93, f: 161.665, l: "b", a: 0},
{o: 161.665, f: 261.495, l: "c", a: 1},
{o: 261.495, f: 327.055, l: "d", a: 0},
{o: 327.055, f: 352.385, l: "e", a: 1},
{o: 352.385, f: 417.2, l: "d", a: 0},
{o: 417.2, f: 419.435, l: "f", a: 1}],
[{o: 0, f: 5.074, l: "n1", a: 0},
{o: 5.074, f: 12.875, l: "B", a: 1},
{o: 12.875, f: 17.554, l: "n2", a: 0},
{o: 17.554, f: 26.924, l: "D", a: 1},
{o: 26.924, f: 38.545, l: "n3", a: 0},
{o: 38.545, f: 46.533, l: "A", a: 1},
{o: 46.533, f: 81.885, l: "n4", a: 0},
{o: 81.885, f: 89.71, l: "B", a: 1},
{o: 89.71, f: 100.635, l: "n5", a: 0},
{o: 100.635, f: 108.658, l: "A", a: 1},
{o: 108.658, f: 117.33, l: "n6", a: 0},
{o: 117.33, f: 126.735, l: "E", a: 1},
{o: 126.735, f: 134.455, l: "B", a: 0},
{o: 134.455, f: 144.544, l: "n8", a: 1},
{o: 144.544, f: 152.66, l: "A", a: 0},
{o: 152.66, f: 175.578, l: "n9", a: 1},
{o: 175.578, f: 185.435, l: "A", a: 0},
{o: 185.435, f: 206.75, l: "n10", a: 1},
{o: 206.75, f: 219.87, l: "A", a: 0},
{o: 219.87, f: 229.216, l: "A", a: 1},
{o: 229.216, f: 255.861, l: "n12", a: 0},
{o: 255.861, f: 266.867, l: "D", a: 1},
{o: 266.867, f: 273.218, l: "n13", a: 0},
{o: 273.218, f: 283.144, l: "A", a: 1},
{o: 283.144, f: 294.975, l: "n14", a: 0},
{o: 294.975, f: 304.181, l: "A", a: 1},
{o: 304.181, f: 310.834, l: "n15", a: 0},
{o: 310.834, f: 318.671, l: "A", a: 1},
{o: 318.671, f: 353.953, l: "n16", a: 0},
{o: 353.953, f: 361.825, l: "A", a: 1},
{o: 361.825, f: 370.962, l: "C", a: 0},
{o: 370.962, f: 380.215, l: "C", a: 1},
{o: 380.215, f: 387.901, l: "E", a: 0},
{o: 387.901, f: 396.179, l: "n18", a: 1},
{o: 396.179, f: 404.05, l: "A", a: 0},
{o: 404.05, f: 419.12, l: "n19", a: 1}],
[{o: 0, f: 0.224, l: "G", a: 0},
{o: 0.224, f: 0.224, l: "B", a: 1},
{o: 0.224, f: 22.56, l: "A", a: 0},
{o: 22.56, f: 206.296, l: "B", a: 1},
{o: 206.296, f: 223.056, l: "F", a: 0},
{o: 223.056, f: 261.416, l: "A", a: 1},
{o: 261.416, f: 343.548, l: "B", a: 0},
{o: 343.548, f: 414.212, l: "F", a: 1},
{o: 414.212, f: 419.345, l: "G", a: 0}]
];

var seriesNames = ["Ground-truth","BV1","BV2","GP7","MHRAF2","MND1","WB1"];

var track_url = "salami000258.ogg";

var artist = "Compilations";

var track = "Basarabye";
