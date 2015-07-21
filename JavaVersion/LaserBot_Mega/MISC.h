const int SCREEN_WIDTH = 320;
const int SCREEN_HEIGHT = 240;

const int TIMEOUR_BLINK_DELAY = 500;
const int TIMEOUR_BLINK_TIMES = 2;

struct Size {
  int width;
  int height;
};

struct Position {
  int x;
  int y;
};

int Touch_X, Touch_Y;
boolean Touch_read() {
  if (Touch.dataAvailable())
  {
    Touch.read();
    Touch_X = Touch.getX();
    Touch_Y = Touch.getY();
    return true;
  }
  return false;
}

boolean inline Touch_isPress() {
  return Touch.dataAvailable();
}

boolean inline MISC_isInBox_XY(int x, int y, int box_x, int box_y, int box_x1, int box_y1) {
  return x > box_x && x < box_x1 && y > box_y && y < box_y1;
}

boolean inline MISC_isTouchBox_XY(int box_x, int box_y, int box_x1, int box_y1) {
  return MISC_isInBox_XY(Touch_X, Touch_Y, box_x, box_y, box_x1, box_y1);
}

boolean inline MISC_isTouchBox(int box_x, int box_y, int width, int height) {
  return MISC_isInBox_XY(Touch_X, Touch_Y, box_x, box_y, box_x+width, box_y+height);
}

void startupScreen_draw() {
  TFT.fillScr(BLACK);
  
  TFT.setColor(WHITE);
  TFT.setFont(BiggerFont);
  TFT.print("LASER BOT", CENTER, 5);
  
  TFT.setColor(YELLOW);
  TFT.print("STARTING", CENTER, 100);
  
  TFT.setColor(RED);
  TFT.setFont(BigFont);
  TFT.print("Copyright @ 2014", CENTER, 208);
  TFT.print("Nguyen Duong Kim Hao", CENTER, 224);
}

void shutdownScreen_draw() {
  TFT.fillScr(BLACK);
  
  TFT.setColor(WHITE);
  TFT.setBackColor(TRANSPARENT);
  TFT.setFont(BiggerFont);
  TFT.print("LASER BOT", CENTER, 5);
  
  TFT.setColor(YELLOW);
  TFT.print("SHUTDOWN", CENTER, 100);
  
  TFT.setColor(RED);
  TFT.setFont(BigFont);
  TFT.print("Copyright @ 2014", CENTER, 208);
  TFT.print("Nguyen Duong Kim Hao", CENTER, 224);
}

