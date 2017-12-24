import React, { Component } from 'react';
let request = require('request-promise-native');
import { ListGroup, ListGroupItem } from 'react-bootstrap';
import toastr from 'toastr';

export default class NewProjectForm extends Component {

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
        let name = this.pname.value || null;
        let budget = parseInt(this.pbudget.value, 10) || null;
        let description = this.pdescription.value || null;
        if (name === null || budget === null || description === null) { return; }
        let query = "SELECT MAX(P.PID) FROM ProjectBudget p";
        let options = {
            uri: 'http://localhost:9000/query/' + encodeURI(query),
            headers: {
                'User-Agent': 'Request-Promise'
            },
            json: true
        };
        request(options)
            .then(function (body) {
                let maxID = body.rows[0]["MAX(P.PID)"] + 1;
                query = "INSERT INTO ProjectBudget VALUES (" + maxID + ", '" + name + "', " + budget + ", '" + description + "')";
                let options = {
                    uri: 'http://localhost:9000/query/' + encodeURI(query),
                    headers: {
                        'User-Agent': 'Request-Promise'
                    },
                    json: true
                };
                request(options)
                    .then(function (body) {
                        toastr.success("Added " + name + " project!");
                    })
                    .catch(function (err) {
                        console.error(err);
                        toastr.error("Failed to add project.");
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
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Project Name</label>
                                        <div className="col-sm-8 col-md-9">
                                            <input type='text' name='name' ref={ref => this.pname = ref} placeholder='Project Name' className="form-control" />
                                        </div>
                                    </div>
                                    <div className="form-group col-sm-6">
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Budget</label>
                                        <div className="col-sm-8 col-md-9">
                                            <input type='number' name='name' ref={ref => this.pbudget = ref} placeholder='$$$$' className="form-control" />
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="form-group col-sm-12">
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Project Description</label>
                                        <div className="col-sm-8 col-md-9">
                                            <textarea className="form-control" ref={ref => this.pdescription = ref} placeholder="Project Description"></textarea>
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
