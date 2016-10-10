bool try_key(char encoded_msg[], int length, char names[][NAME_SIZE], int key)
{
  char encoded_mall[13];
  for (int i=0; i<NUMBER_OF_MALLS; i++)
    {
      encode(key, names[i], 13, encoded_mall);
      if(find_text_anywhere(encoded_msg, length, encoded_mall, 13))
         return true;
    }
};
