#include <iostream>
using namespace std;

const int ASCII_LIMIT = 128;

const int NUMBER_OF_MALLS = 7;
const int MALL_NAME_LENGTH = 13;

//allow an additional character for a null terminator beyond mall name length
const int NAME_SIZE = MALL_NAME_LENGTH + 1;

//Functions for which you must write definitions

void encode(int key, char plain[], int length, char encoded[])
{
  for (int i=0; i<length;i++)
  {
    if (plain[i]+key>128)
      encoded[i]=plain[i]+key-128;
    else
      encoded[i]=plain[i]+key;
  }
};

void decode(int key, char encoded[], int length, char plain[])
{
  for (int i=0; i<length;i++)
  {
    if (encoded[i]-key<0)
      plain[i]=encoded[i]-key+128;
    else
      plain[i]=encoded[i]-key;
  }
};

// look for a match to text at this location in message
bool find_text_here(char msg[], int here, char text[], int text_length)
{
  int total = 0;
  for (int i=0; i<text_length; i++)
  { 
    if (msg[here+i]==text[i])
      total=total+1;
    else
      total = 0;
  }
  if (total==text_length)
    return true;
  else 
    return false;
};

// look for first match to text anywhere in message
bool find_text_anywhere(char msg[], int msg_length, char text[], int text_length)
{
  for (int i = 0; i<msg_length; i++)
  { if (find_text_here(msg, i, text, text_length))
      return true;
  }
  return false;
};

bool try_key(char code[], int length, char names[][NAME_SIZE], int key)
{
  char encoded_mall[13];
  for (int i=0; i<NUMBER_OF_MALLS; i++)
    {
      encode(key, names[i], 13, encoded_mall);
      if(find_text_anywhere(code, length, encoded_mall, 13))
         return true;
    }

};

int main()
{
   const int MESSAGE_LENGTH = 140;

   char MALL_NAMES[NUMBER_OF_MALLS][NAME_SIZE] = {"1881 Heritage",
                                                  "Festival Walk",
                                                  "Horizon Plaza",
                                                  "Langham Place",
                                                  "Olympian City",
                                                  "Pacific Place",
                                                  "Telford Plaza"};

   //sample coded message (contains encoded text "Olympian City")
   char coded_message[MESSAGE_LENGTH] =
     {12, 51, 47, 4, 45, 11, 81, 4, 73, 82, 83, 89, 75, 76, 
        4, 91, 77, 88, 76, 4, 91, 86, 77, 88, 77, 82, 75, 4, 
        88, 76, 73, 87, 73, 4, 88, 76, 77, 82, 75, 87, 18, 18, 
        18, 13, 70, 80, 69, 76, 17, 70, 80, 69, 76, 17, 70, 
        80, 69, 76, 17, 70, 80, 69, 76, 17, 70, 80, 69, 76, 17, 
        70, 80, 69, 76, 17, 70, 80, 69, 76, 17, 70, 80, 69, 76, 
        17, 70, 80, 69, 76, 17, 70, 80, 69, 76, 17, 70, 80, 69, 
        76, 17, 70, 80, 69, 76, 17, 70, 80, 69, 76, 17, 70, 80, 
        69, 76, 17, 70, 80, 69, 76, 17, 70, 80, 69, 76, 70, 80, 
        69, 76, 51, 80, 93, 81, 84, 77, 69, 82, 4, 39, 77, 
        88, 93};

   for (int key = 0; key < ASCII_LIMIT; ++key)
   {
      if (try_key(coded_message, MESSAGE_LENGTH, MALL_NAMES, key))
      {
         cout << "Code broken with key: " << key << endl;

         char decoded_message[MESSAGE_LENGTH];
         decode(key, coded_message, MESSAGE_LENGTH, decoded_message);

         cout << "\nDecoded message is:" << endl;
         for (int i = 0; i < MESSAGE_LENGTH; ++i)
         {
            cout << decoded_message[i];
         }
         cout << endl;

         return 0;
      }
   }
   cout << "Failed to decode" << endl;
   return 0;
}