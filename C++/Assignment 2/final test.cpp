#include <iostream>
using namespace std;

const int ASCII_LIMIT = 128;
const int NUMBER_OF_MALLS = 7;
const int MALL_NAME_LENGTH = 13;
const int NAME_SIZE = MALL_NAME_LENGTH + 1;

void decode(int key, char encoded[], int length, char plain[]);
void encode(int key, char plain[], int length, char encoded[]);
bool find_text_anywhere(char msg[], int msg_length, char text[], int text_length);
bool find_text_here(char msg[], int here, char text[], int text_length);
bool try_key(char code[], int length, char names[][NAME_SIZE], int key);

#include "encode.cpp"
#include "decode.cpp"
#include "find_text_here.cpp"
#include "try_key.cpp"
#include "find_text_anywhere.cpp"

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