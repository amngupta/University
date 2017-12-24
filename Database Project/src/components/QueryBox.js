import React, { Component } from 'react';
let request = require('request-promise-native');
import { Button, Col, Grid } from 'react-bootstrap';
import JsonTable from 'react-json-table';
import toastr from 'toastr';

export default class QueryBox extends Component {

    constructor(props) {
        super(props);
        this.state = {
            errors: {},
            rows: []
        };
        this.doQuery = this.doQuery.bind(this);
    }


    doQuery() {
        let self = this;
        let query = this.queryBox.value;
        console.log(query);
        var options = {
            uri: 'http://localhost:9000/query/' + encodeURI(query),
            headers: {
                'User-Agent': 'Request-Promise'
            },
            json: true // Automatically parses the JSON string in the response 
        };
        request(options)
            .then(function (body) {
                console.log(body);
                toastr.success("Query success!");
                let columns = [];
                body.metaData.forEach((mD) => {
                    columns.push(mD.name);
                })
                self.setState({
                    rows: body.rows, columnNames: columns
                });
                // console.log(self.state.rows);
            })
            .catch(function (err) {
                // API call failed...
                toastr.error("Query failed...");
                console.error(err);
            });
    }

    render() {
        return (
            <Grid fluid={true}>
                <Col xs={12}>
                    <h1>Welcome to Scrooge McDuck Project Management </h1>
                    <h3>Use queryBox below or use the interface...</h3>
                </Col>
                <Col xs={10}>
                    <textarea ref={ref => this.queryBox = ref} className="App-intro">

                    </textarea>
                </Col>
                <Col xs={2}>
                    <Button onClick={() => { this.doQuery() }} bsStyle="success">
                        GO
            </Button>
                </Col>
                <Col xs={12}>
                    <div className="table-responsive">
                        <JsonTable className="table" rows={this.state.rows} />
                    </div>
                </Col>
            </Grid>
        );
    }
}
