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