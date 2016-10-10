bool find_text_anywhere(char msg[], int msg_length, char text[], int text_length)
{
  for (int i = 0; i<msg_length; i++)
  { if (find_text_here(msg, i, text, text_length))
      return true;
  }
  return false;
};