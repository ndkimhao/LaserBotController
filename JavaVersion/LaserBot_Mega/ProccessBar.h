const int PROCCESSBAR_X = 10;
const int PROCCESSBAR_Y = 220;
const int PROCCESSBAR_TEXTOFFSET = 16*4.5;
const int PROCCESSBAR_WIDTH = SCREEN_WIDTH - PROCCESSBAR_X*2 - PROCCESSBAR_TEXTOFFSET;
const int PROCCESSBAR_HEIGHT = 16;
const int PROCCESSBAR_X2 = SCREEN_WIDTH - PROCCESSBAR_X;
const int PROCCESSBAR_Y2 = PROCCESSBAR_Y + PROCCESSBAR_HEIGHT;

word ProccessBar_textColor;
word ProccessBar_fullColor;
word ProccessBar_emptyColor;
word ProccessBar_backColor;

void ProccessBar_init(word backColor, word textColor, word fullColor, word emptyColor) {
  ProccessBar_backColor = backColor;
  ProccessBar_textColor = textColor;
  ProccessBar_fullColor = fullColor;
  ProccessBar_emptyColor = emptyColor;
  TFT.setColor(ProccessBar_emptyColor);
  TFT.fillRoundRect(PROCCESSBAR_X+PROCCESSBAR_TEXTOFFSET, PROCCESSBAR_Y, PROCCESSBAR_X2, PROCCESSBAR_Y2);
}

void ProccessBar_init(word backColor) {
  ProccessBar_backColor = backColor;
  ProccessBar_textColor = RED;
  ProccessBar_fullColor = RED;
  ProccessBar_emptyColor = YELLOW;
  TFT.setBackColor(backColor);
  TFT.setColor(ProccessBar_emptyColor);
  TFT.fillRect(PROCCESSBAR_X+PROCCESSBAR_TEXTOFFSET, PROCCESSBAR_Y, PROCCESSBAR_X2, PROCCESSBAR_Y2);
}

void ProccessBar_draw(byte percent) {
  TFT.setColor(ProccessBar_textColor);
  TFT.setFont(BigFont);
  TFT.printNumI(percent, PROCCESSBAR_X, PROCCESSBAR_Y, 3);
  TFT.print("%", PROCCESSBAR_X+16*3, PROCCESSBAR_Y);

  TFT.setColor(ProccessBar_fullColor);
  TFT.fillRoundRect(PROCCESSBAR_X+PROCCESSBAR_TEXTOFFSET, PROCCESSBAR_Y,
  PROCCESSBAR_X+PROCCESSBAR_TEXTOFFSET + (PROCCESSBAR_WIDTH * (int)percent / 100), PROCCESSBAR_Y2);
}






