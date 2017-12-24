import React, { Component } from 'react';
let request = require('request-promise-native');
import { Grid, Row, Col, Modal, ListGroup, ControlLabel, FormControl, Button, ListGroupItem } from 'react-bootstrap';
import { NavLink } from 'react-router-dom'
import JsonTable from 'react-json-table';
import ModalOpen from '../Modal'
import NewProjectForm from './newProjectForm'
import toastr from 'toastr';



export default class ViewProjects extends Component {

    constructor(props) {
        super(props);
        this.state = {
            errors: {},
            rows: [],
            aggregation: [],
            showModal: false
        };
        this.close = this.close.bind(this);
        this.open = this.open.bind(this);
        this.doQuery = this.doQuery.bind(this);
        this.onClickRow = this.onClickRow.bind(this);
        this.onClickUpdate = this.onClickUpdate.bind(this);
        this.onClickDelete = this.onClickDelete.bind(this);
        this.onClickView = this.onClickView.bind(this);
    }

    componentWillMount() {
        let query = "SELECT * from ProjectBudget";
        let options = {
            uri: 'http://localhost:9000/query/' + encodeURI(query),
            headers: {
                'User-Agent': 'Request-Promise'
            },
            json: true
        };
        let self = this;
        request(options)
            .then(function (body) {
                self.setState({
                    rows: body.rows
                });
                query = "SELECT COUNT(*) FROM ProjectBudget";
                options = {
                    uri: 'http://localhost:9000/query/' + encodeURI(query),
                    headers: {
                        'User-Agent': 'Request-Promise'
                    },
                    json: true
                };
                return request(options)
            })
            .then(function (body) {
                console.log(body);
                for (let i = 0; i < body.rows.length; i++) {
                    body.rows[i]["COUNT"] = body.rows[i]["COUNT(*)"];
                    delete body.rows[i]["COUNT(*)"];
                }
                toastr.success("Entries loaded successfully!");

                self.setState({
                    aggregation: body.rows
                });
            })
            .catch(function (err) {
                console.error(err);
                toastr.error("Failed to load entries...");
            });
    }

    close() {
        this.setState({ showModal: false });
    }

    open() {
        this.setState({ showModal: true });
    }

    onClickRow(e, data) {
        console.log(e);
        console.log(data);
        this.open();
        this.setState({ modalData: data });
    }

    doQuery(e) {
        if (e) { e.preventDefault(); }
        let id = this.pid.value || null;
        let name = this.pname.value || null;
        let aggr = this.waggr.value || null;
        let querySuffix = " FROM ProjectBudget p";
        let filter = [];
        if (id) { filter.push("p.pid=" + id); }
        if (name) { filter.push("LOWER(p.name) LIKE LOWER('%" + name + "%')"); }
        if (filter.length !== 0) { querySuffix += " WHERE " + filter.join(" AND "); }
        let self = this;
        let options = {
            uri: 'http://localhost:9000/query/' + encodeURI("SELECT *" + querySuffix),
            headers: {
                'User-Agent': 'Request-Promise'
            },
            json: true
        };
        request(options)
            .then(function (body) {
                console.log(body);
                self.setState({
                    rows: body.rows
                });
                let aggrQuery = "";
                switch (aggr) {
                    case "COUNT":
                        aggrQuery = aggr + "(*)";
                        break;
                    case "AVG":
                    case "MAX":
                    case "MIN":
                    case "SUM":
                        aggrQuery = aggr + "(P.BUDGET)";
                        break;
                    default:
                        break;
                }
                let options = {
                    uri: 'http://localhost:9000/query/' + encodeURI("SELECT " + aggrQuery + querySuffix),
                    headers: {
                        'User-Agent': 'Request-Promise'
                    },
                    json: true
                };
                request(options)
                    .then(function (body) {
                        console.log(body);
                        for (let i = 0; i < body.rows.length; i++) {
                            body.rows[i][aggr] = body.rows[i][aggrQuery];
                            delete body.rows[i][aggrQuery];
                        }
                        self.setState({
                            aggregation: body.rows
                        });
                        toastr.success("Search success!");
                    })
                    .catch(function (err) {
                        console.error(err);
                    });
            })
            .catch(function (err) {
                console.error(err);
                toastr.error("Search failed...");
            });
    }

    onClickUpdate() {
        let pid = this.peditId.value || null;
        if (!pid) { return (console.error("PID is null...")); }
        let name = this.peditName.value || null;
        let budg = this.peditBudget.value || null;
        let desc = this.pdescription.value || null;

        let update = "";
        if (name && (name !== this.state.modalData.NAME)) {
            update = "p.name='" + name + "'";
        }
        if (budg && (budg !== this.state.modalData.BUDGET)) {
            let set = "p.budget='" + budg + "'";
            update = update ? update + ", " + set : set;
        }
        if (desc && (desc !== this.state.modalData.DESCRIPTION)) {
            let set = "p.description='" + desc + "'";
            update = update ? update + ", " + set : set;
        }
        if (!update) { return (toastr.warning("No changes to update...")); }

        let query = "UPDATE projectbudget p SET " + update + " WHERE p.pid=" + pid;
        let self = this;
        let options = {
            uri: 'http://localhost:9000/query/' + encodeURI(query),
            headers: {
                'User-Agent': 'Request-Promise'
            },
            json: true
        };
        request(options)
            .then(function (body) {
                console.log(body);
                toastr.success("Update success!");
                self.doQuery();
                self.close();
            })
            .catch(function (err) {
                toastr.error("Update failed...");
                console.error(err);
            });
    }

    onClickDelete() {
        let id = this.peditId.value || null;
        if (!id) { return (console.error("ID is null...")); }

        let query = "DELETE FROM projectbudget p WHERE p.pid=" + id;
        let self = this;
        let options = {
            uri: 'http://localhost:9000/query/' + encodeURI(query),
            headers: {
                'User-Agent': 'Request-Promise'
            },
            json: true
        };
        request(options)
            .then(function (body) {
                console.log(body);
                self.doQuery();
                self.close();
            })
            .catch(function (err) {
                console.error(err);
            });
    }

    onClickView() {
        window.location = "/president/workers?pid=" + this.state.modalData.PID;
    }

    render() {
        const button = (<Button bsStyle="success">Add New Project </Button>)
        const newProjectForm = <NewProjectForm />

        const formBody = (
            <div>
                <form className="form-horizontal" onSubmit={this.doQuery}>
                    <fieldset>
                        <ListGroup>
                            <ListGroupItem>
                                <div className="row">
                                    <div className="form-group col-sm-6">
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Id Number</label>
                                        <div className="col-sm-8 col-md-9">
                                            <input type='number' name='name' ref={ref => this.pid = ref} placeholder='ID Number' className="form-control" />
                                        </div>
                                    </div>
                                    <div className="form-group col-sm-6">
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Project Name</label>
                                        <div className="col-sm-8 col-md-9">
                                            <input type='text' name='name' ref={ref => this.pname = ref} placeholder='Project Name' className="form-control" />
                                        </div>
                                    </div>
                                    <div className="form-group col-sm-6">
                                        <ControlLabel className="col-sm-4 col-md-3">Aggregation:</ControlLabel>
                                        <div className="col-sm-8 col-md-9">
                                            <FormControl componentClass="select" inputRef={ref => this.waggr = ref}>
                                                <option value="COUNT">Count</option>
                                                <option value="AVG">Average Budget</option>
                                                <option value="MAX">Max Budget</option>
                                                <option value="MIN">Min Budget</option>
                                                <option value="SUM">Sum of Budgets</option>
                                            </FormControl>
                                        </div>
                                    </div>
                                    <div className="form-group col-sm-12">
                                        <div className="form-group col-sm-12">
                                            <div className=" col-xs-12 text-center">
                                                <input type="submit" className="btn btn-success" value="Search" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </ListGroupItem>
                        </ListGroup>
                    </fieldset>
                </form>
            </div>
        )
        let modalBody = null;
        if (this.state.modalData) {
            modalBody = (
                <div>
                    <form className="form-horizontal" onSubmit={this.doQuery}>
                        <fieldset>
                            <div className="row">
                                <div className="form-group col-sm-6">
                                    <label className="control-label text-semibold col-sm-4 col-md-3">Id Number</label>
                                    <div className="col-sm-8 col-md-9">
                                        <input type='number' disabled name='name' ref={ref => this.peditId = ref} placeholder='ID Number' defaultValue={this.state.modalData.PID} className="form-control" />
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="form-group col-sm-6">
                                    <label className="control-label text-semibold col-sm-4 col-md-3">Budget</label>
                                    <div className="col-sm-8 col-md-9">
                                        <input type='number' name='name' ref={ref => this.peditBudget = ref} placeholder='1234' defaultValue={this.state.modalData.BUDGET} className="form-control" />
                                    </div>
                                </div>
                                <div className="form-group col-sm-6">
                                    <label className="control-label text-semibold col-sm-4 col-md-3">Project Name</label>
                                    <div className="col-sm-8 col-md-9">
                                        <input type='text' name='name' ref={ref => this.peditName = ref} placeholder='Project Name' defaultValue={this.state.modalData.NAME} className="form-control" />
                                    </div>
                                </div>
                                <div className="form-group col-sm-12">
                                    <label className="control-label text-semibold col-sm-4 col-md-3">Project Description</label>
                                    <div className="col-sm-8 col-md-9">
                                        <textarea ref={ref => this.pdescription = ref} className="form-control" placeholder="Project Description" defaultValue={this.state.modalData.DESCRIPTION} maxLength={1000} ></textarea>
                                    </div>
                                </div>
                            </div>
                        </fieldset>
                    </form>
                </div>
            )
        }

        return (
            <Grid fluid={true}>
                <Modal show={this.state.showModal} bsSize="large" onHide={this.close}>
                    <Modal.Header closeButton>
                    </Modal.Header>
                    <Modal.Body>
                        {modalBody}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button bsStyle="info" onClick={this.onClickView}>
                            View project workers
                        </Button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <Button bsStyle="success" onClick={this.onClickUpdate}>
                            Update
                        </Button>
                        <Button bsStyle="danger" onClick={this.onClickDelete}>
                            Delete
                        </Button>
                    </Modal.Footer>
                </Modal>
                <Row>
                    <Col xs={10}>
                        {formBody}
                    </Col>
                    <Col xs={2}>
                        <ModalOpen eventListener={button} modalBody={newProjectForm} />
                    </Col>
                </Row>
                <Row>
                    <Col xs={12}>
                        <div className="table-responsive">
                            <JsonTable className="table" rows={this.state.aggregation} />
                        </div>
                    </Col>
                    <Col xs={12}>
                        <div className="table-responsive">
                            <JsonTable className="table" rows={this.state.rows} onClickRow={this.onClickRow} />
                        </div>
                    </Col>
                </Row>
            </Grid>
        )
    }
}
