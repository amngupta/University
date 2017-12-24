import React, { Component } from 'react';
let request = require('request-promise-native');
import { ListGroup, ControlLabel, FormControl, ListGroupItem } from 'react-bootstrap';
import toastr from 'toastr';

export default class NewWorkerForm extends Component {

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
        let table = this.wtype.value;
        let name = this.wname.value || null;
        let phone = this.wphone.value || null;
        if (name === null || phone === null) { return; }
        let query = "SELECT MAX(w.id) FROM worker w";
        let options = {
            uri: 'http://localhost:9000/query/' + encodeURI(query),
            headers: {
                'User-Agent': 'Request-Promise'
            },
            json: true
        };
        request(options)
            .then(function (body) {
                let maxID = body.rows[0]["MAX(W.ID)"] + 1;
                query = "INSERT INTO Worker VALUES (" + maxID + ", '" + name + "', '" + phone + "')";
                let options = {
                    uri: 'http://localhost:9000/query/' + encodeURI(query),
                    headers: {
                        'User-Agent': 'Request-Promise'
                    },
                    json: true
                };
                request(options)
                    .then(function (body) {
                        switch (table) {
                            case "employee":
                                query = "INSERT INTO employee VALUES (" + maxID + ", 11)";
                                break;
                            case "manager":
                                query = "INSERT INTO manager VALUES (" + maxID + ", 1)";
                                break;
                            case "president":
                                query = "INSERT INTO president VALUES (" + maxID + ")";
                                break;
                            default:
                                break;
                        }
                        let options = {
                            uri: 'http://localhost:9000/query/' + encodeURI(query),
                            headers: {
                                'User-Agent': 'Request-Promise'
                            },
                            json: true
                        };
                        request(options)
                            .then(function (body) {
                                toastr.success("Added " + table + "!");
                            })
                            .catch(function (err) {
                                console.error(err);
                            });
                    })
                    .catch(function (err) {
                        console.error(err);
                        toastr.error("Error; please try different fields.")
                    });
            })
            .catch(function (err) {
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
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Full Name</label>
                                        <div className="col-sm-8 col-md-9">
                                            <input type='text' name='name' ref={ref => this.wname = ref} placeholder='Full Name' className="form-control" />
                                        </div>
                                    </div>
                                    <div className="form-group col-sm-6">
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Phone Number</label>
                                        <div className="col-sm-8 col-md-9">
                                            <input type='text' name='name' ref={ref => this.wphone = ref} placeholder='111-222-3333' className="form-control" />
                                        </div>
                                    </div>
                                    <div className="form-group col-sm-6">
                                        <ControlLabel className="col-sm-8 col-md-9">Type</ControlLabel>
                                        <div className="col-sm-8 col-md-9 col-md-offset-9">
                                            <FormControl componentClass="select" inputRef={ref => this.wtype = ref} placeholder="select">
                                                <option value="employee">Employee</option>
                                                <option value="manager">Manager</option>
                                                <option value="president">President</option>
                                            </FormControl>
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
