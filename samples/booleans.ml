{
  var x = 4;
  var y = 7;

  if ((x < y) && (y > 0)) {
    print(true);
  } else {
    print(false);
  }

  if ((x == 4) || (y == 0)) {
    print(true);
  } else {
    print(false);
  }

  print(!(x > y));  // true
}
