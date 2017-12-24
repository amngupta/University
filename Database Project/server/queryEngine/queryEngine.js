var oracledb = require('oracledb');
oracledb.autoCommit = true;
import dbConfig from '../../config';


class queryEngine {

    // export LD_LIBRARY_PATH=/opt/oracle/instantclient:$LD_LIBRARY_PATH
    // ssh -l t0d1b -L localhost:1522:dbhost.ugrad.cs.ubc.ca:1522 remote.ugrad.cs.ubc.ca

    performQuery(request) {
        let connection;
        // console.log("Reached init", request);
        // Get a non-pooled connection
        return new Promise((fulfill, reject) => {
            oracledb.getConnection({
                user: dbConfig.user,
                password: dbConfig.password,
                connectString: dbConfig.connecturl
            })
                .then(conn => {
                    connection = conn;
                    return conn.execute(request, [], { outFormat: oracledb.OBJECT });
                })
                .then(result => {
                    this.close(connection);
                    fulfill(result);
                })
                .catch(err => {
                    console.log(err);
                    this.close(connection);
                    reject(err);
                })
        })

    }

    close(connection) {
        connection.close(
            function (err) {
                if (err) {
                    console.error(err.message);
                }
            });
    }
}

module.exports = queryEngine;