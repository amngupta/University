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