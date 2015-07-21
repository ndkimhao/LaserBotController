const byte BUTTON_MAX = 12;
const byte BUTTON_AUTO = 0;

word Button_backColor[BUTTON_MAX];
word Button_pressBackColor[BUTTON_MAX];
word Button_textColor[BUTTON_MAX];
word Button_postion[BUTTON_MAX][2];
word Button_textPostion[BUTTON_MAX][2];
word Button_size[BUTTON_MAX][2];
boolean Button_state[BUTTON_MAX];
char* Button_text[BUTTON_MAX];

struct ButtonStruct {
  char* text;
  Position boxPos;
  Position boxPos1;
  Position textPos;
  word textColor;
  word backColor;
  word pressBackColor;
  boolean state;
};

ButtonStruct Button_data[BUTTON_MAX];
byte Button_count = 0;

byte Button_addNew(char* text, int x, int y, int w, int h, word textColor, word backColor, word pressBackColor) {
  ButtonStruct* newButton = &Button_data[Button_count++];
  newButton->text = text;
  newButton->boxPos.x = x;
  newButton->boxPos.y = y;
  w = (w == BUTTON_AUTO ? strlen(text)*16+16 : w);
  h = (h == BUTTON_AUTO ? 48 : h);
  newButton->boxPos1.x = x + w;
  newButton->boxPos1.y = y + h;
  newButton->textPos.x = x + (w - strlen(text)*16) / 2;
  newButton->textPos.y = y + (h - 16) / 2;
  newButton->textColor = textColor;
  newButton->backColor = backColor;
  newButton->pressBackColor = pressBackColor;
  newButton->state = false;
  return Button_count - 1;
}

void Button_clear() {
  Button_count = 0;
}

boolean Button_isPress(byte index) {
  return Button_state[index];
}

char Button_getPressedButton() {
  for(byte i = 0; i < Button_count; i++) {
    if(Button_data[i].state) return i;
  }
  return -1;
}

void Button_draw() {
  TFT.setFont(BigFont);
  for(byte i = 0; i < Button_count; i++) {
    ButtonStruct* curButton = &Button_data[i];

    TFT.setColor(curButton->backColor);
    TFT.fillRoundRect(curButton->boxPos.x, curButton->boxPos.y, curButton->boxPos1.x, curButton->boxPos1.y);

    TFT.setColor(curButton->textColor);
    TFT.print(curButton->text, curButton->textPos.x, curButton->textPos.y);
  }
}

void Button_proccess() {
  static boolean prevState = false;
  boolean curState = Touch_read();
  if(curState != prevState) {
    prevState = curState;
    TFT.setFont(BigFont);
    for(byte i = 0; i < Button_count; i++) {
      ButtonStruct* curButton = &Button_data[i];
      int posX = curButton->boxPos.x;
      int posY = curButton->boxPos.y;
      int posX1 = curButton->boxPos1.x;
      int posY1 = curButton->boxPos1.y;
      boolean state = curButton->state;
      if(!state && MISC_isTouchBox_XY(posX, posY, posX1, posY1)){
        curButton->state = true;

        TFT.setColor(curButton->pressBackColor);
        TFT.fillRoundRect(posX, posY, posX1, posY1);

        TFT.setColor(curButton->textColor);
        TFT.print(curButton->text, curButton->textPos.x, curButton->textPos.y);
      }
      else if(state) {
        curButton->state = false;

        TFT.setColor(curButton->backColor);
        TFT.fillRoundRect(posX, posY, posX1, posY1);

        TFT.setColor(curButton->textColor);
        TFT.print(curButton->text, curButton->textPos.x, curButton->textPos.y);
      }
    }
  }
}









