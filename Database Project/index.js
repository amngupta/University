var oracledb = require('oracledb');
import * as fs from 'fs';
import dbConfig from './config';

// export LD_LIBRARY_PATH=/opt/oracle/instantclient:$LD_LIBRARY_PATH
// ssh -l t0d1b -L localhost:1522:dbhost.ugrad.cs.ubc.ca:1522 remote.ugrad.cs.ubc.ca

// Get a non-pooled connection
var connection;

let initString = () => {
  return new Promise((fulfill, reject) => {
    fs.readFile("Project_DDL.sql", 'utf-8', (err, data) => {
      if (!err) {
        data.toString('ascii', 0, data.length);
        let commands = data.split(";");
        fulfill(commands);
      }
    });
  });
}

// Get a non-pooled connection
oracledb.getConnection({
  user: dbConfig.user,
  password: dbConfig.password,
  connectString: dbConfig.connecturl
})
  .then(conn => {
    connection = conn;
    return initString()
      .then(commands => {
        commands.forEach(command => {
          return conn.execute(command)
        })
        return conn.execute("SELECT table_name FROM user_tables");
      });
  })
  .then(result => {
    console.log(result.metaData); // [ { name: 'DEPARTMENT_ID' }, { name: 'DEPARTMENT_NAME' } ]
    console.log(result.rows);     // [ [ 180, 'Construction' ] ]
    doRelease(connection);
  })
  .catch(err => {
    console.log(err);
    doRelease(connection);
  })

// Note: connections should always be released when not needed
var doRelease = (conn) => {
  conn.close(
    function (err) {
      if (err) {
        console.error(err.message);
      }
    });
}