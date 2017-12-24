import React, { Component } from 'react';
let request = require('request-promise-native');
import { ListGroup, ListGroupItem } from 'react-bootstrap';
let moment = require('moment');
import toastr from 'toastr';

export default class NewExpenditureForm extends Component {

    constructor(props) {
        super(props);
        this.state = {
            errors: {},
            rows: []
        };
        this.doQuery = this.doQuery.bind(this);
    }


    doQuery(e) {
        e.preventDefault();
        let id = this.pid.value || null;
        let type = this.etype.value || null;
        let amt = this.eamount.value || null;
        let desc = this.edescription.value || null;

        if (id === null || type === null || amt === null || desc === null) { return (toastr.warning("Please fill in all the boxes!")); }
        let query = "SELECT MAX(e.eid) FROM expenditure e";
        let options = {
            uri: 'http://localhost:9000/query/' + encodeURI(query),
            headers: {
                'User-Agent': 'Request-Promise'
            },
            json: true
        };
        let maxID;
        let wid;
        request(options)
            .then(function (body) {
                maxID = body.rows[0]["MAX(E.EID)"] + 1;
                wid = 11; // TODO: should be passed in...
                let date = "to_date('" + moment().format("YYYY-MM-DD HH:MM:ss") + "', 'YYYY-MM-DD HH24:MI:SS')";
                console.log(date);

                query = "INSERT INTO Expenditure VALUES (" + maxID + ", '" + type + "', '" + desc + "', " + date + ", " + amt + ")";
                let options = {
                    uri: 'http://localhost:9000/query/' + encodeURI(query),
                    headers: {
                        'User-Agent': 'Request-Promise'
                    },
                    json: true
                };
                return request(options)
            })
            .then(function (body) {
                query = "INSERT INTO ExpenditureManager VALUES (" + maxID + ", " + id + ", " + wid + ")";
                let options = {
                    uri: 'http://localhost:9000/query/' + encodeURI(query),
                    headers: {
                        'User-Agent': 'Request-Promise'
                    },
                    json: true
                };
                return request(options)
            })
            .then(function (body) {
                toastr.success("Added " + type + " expendture!");
            })
            .catch(function (err) {
                toastr.error("Failed to add...");
                console.error(err);
            });
    }

    render() {
        const formBody = (
            <div>
                <form className="form-horizontal" onSubmit={this.doQuery}>
                    <fieldset>
                        <ListGroup>
                            <ListGroupItem>
                                <div className="row">
                                    <div className="form-group col-sm-6">
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Project Id</label>
                                        <div className="col-sm-8 col-md-9">
                                            <input type='number' name='name' ref={ref => this.pid = ref} placeholder='Project Id' className="form-control" />
                                        </div>
                                    </div>
                                    <div className="form-group col-sm-6">
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Expenditure Type</label>
                                        <div className="col-sm-8 col-md-9">
                                            <input type='text' name='name' ref={ref => this.etype = ref} placeholder='Type' className="form-control" />
                                        </div>
                                    </div>
                                    <div className="form-group col-sm-6">
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Amount</label>
                                        <div className="col-sm-8 col-md-9">
                                            <input type='number' ref={ref => this.eamount = ref} placeholder='Amount' className="form-control" />
                                        </div>
                                    </div>
                                    <div className="form-group col-sm-6">
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Description</label>
                                        <div className="col-sm-8 col-md-9">
                                            <textarea className="form-control" ref={ref => this.edescription = ref} placeholder="Expenditure Description"></textarea>
                                        </div>
                                    </div>
                                </div>
                            </ListGroupItem>
                            <ListGroupItem>
                                <div className="row">
                                    <div className="form-group col-sm-12">
                                        <div className=" col-xs-12 text-center">
                                            <input type="submit" className="btn btn-success" />
                                        </div>
                                    </div>
                                </div>
                            </ListGroupItem>
                        </ListGroup>
                    </fieldset>

                </form>
            </div>
        )

        return (
            formBody
        );
    }
}
