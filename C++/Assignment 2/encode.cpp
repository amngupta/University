void encode(int key, char plain[], int length, char encoded[])
{
  for (int i=0; i<length;i++)
  {
    if (plain[i]+key>=128)
      encoded[i]=plain[i]+key-128;
    else
      encoded[i]=plain[i]+key;
  }
};
