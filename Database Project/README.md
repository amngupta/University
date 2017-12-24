# oracleProject

### To Install: 
1. Export path variable using `export LD_LIBRARY_PATH=/opt/oracle/instantclient:$LD_LIBRARY_PATH`
2. Ensure you are connected to the remote database on port 1522
3. Run `yarn upgrade`
4. Run `yarn build`
5. Run `yarn start-server`

### To populate databse:
1. Ensure you are connected to the database 
2. Ensure you have babel-cli installed or install using `npm install -g babel-cli`
3. Run the script `yarn reset-oracle` 

