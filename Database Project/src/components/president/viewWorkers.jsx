import React, { Component } from 'react';
let request = require('request-promise-native');
import { Grid, Row, Modal, Col, ListGroup, ControlLabel, FormControl, Button, ListGroupItem } from 'react-bootstrap';

import JsonTable from 'react-json-table';
import ModalOpen from '../Modal'
import NewWorkerForm from './newWorkerForm'
import toastr from 'toastr';

const queryString = require('query-string');

export default class ViewWorkers extends Component {

    constructor(props) {
        super(props);
        this.state = {
            errors: {},
            rows: [],
            showModal: false
        };
        this.close = this.close.bind(this);
        this.open = this.open.bind(this);
        this.doQuery = this.doQuery.bind(this);
        this.onClickRow = this.onClickRow.bind(this);
        this.onClickUpdate = this.onClickUpdate.bind(this);
        this.onClickDelete = this.onClickDelete.bind(this);
    }

    componentWillMount() {
        const parsed = queryString.parse(this.props.location.search);
        let projectIDParam = parsed.pid || null;
        let query = "";
        console.log(projectIDParam);
        if (projectIDParam === null) {
            query = "SELECT * FROM worker";
        } else {
            query = "SELECT pb.name AS proj_name , w.id, w.name, w.phonenumber, pa.role " +
                "FROM worker w, employee e, ProjectAssignedToEmployee pa, ProjectBudget pb " +
                "WHERE w.id=e.emp_id AND e.emp_id=pa.emp_id AND pa.pid=pb.pid AND pa.pid=" + projectIDParam;
        }
        let options = {
            uri: 'http://localhost:9000/query/' + encodeURI(query),
            headers: {
                'User-Agent': 'Request-Promise'
            },
            json: true // Automatically parses the JSON string in the response
        };
        let self = this;
        request(options)
            .then(function (body) {
                console.log(body);
                self.setState({
                    rows: body.rows
                });
                toastr.success("Entries loaded successfully!");
            })
            .catch(function (err) {
                // API call failed...
                toastr.error("Failed to load entries...");
                console.error(err);
            });
    }

    doQuery(e) {
        if (e) { e.preventDefault(); }
        let table = this.wtype.value;
        let table_id;
        switch (table) {
            case "worker":
                table = "";
                table_id = "w.id";
                break;
            case "employee":
                table = ", employee e";
                table_id = "e.emp_id";
                break;
            case "manager":
                table = ", manager m";
                table_id = "m.man_id";
                break;
            case "president":
                table = ", president p";
                table_id = "p.pres_id";
                break;
            default:
                break;
        }
        let filter = [];
        let id = this.wid.value || null;
        let name = this.wname.value || null;
        let phone = this.wphone.value || null;
        if (id) {
            filter.push("w.id=" + id);
        }
        if (name) {
            filter.push("LOWER(w.name) LIKE LOWER('%" + name + "%')");
        }
        if (phone) {
            filter.push("w.phonenumber LIKE '%" + phone + "%'");
        }
        let query = "SELECT * FROM worker w" + table + " WHERE w.id=" + table_id;
        if (filter.length !== 0) {
            query += " AND " + filter.join(" AND ");
        }
        let self = this;
        let options = {
            uri: 'http://localhost:9000/query/' + encodeURI(query),
            headers: {
                'User-Agent': 'Request-Promise'
            },
            json: true // Automatically parses the JSON string in the response
        };
        request(options)
            .then(function (body) {
                console.log(body);
                self.setState({
                    rows: body.rows
                });
                toastr.success("Search success!");
            })
            .catch(function (err) {
                // API call failed...
                toastr.error("Search failed...");
                console.error(err);
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

    onClickUpdate() {
        let id = this.peditId.value || null;
        if (!id) { return (console.error("ID is null...")); }
        let name = this.peditName.value || null;
        let phone = this.peditPhone.value || null;

        let update = "";
        if (name && (name !== this.state.modalData.NAME)) {
            update = "w.name='" + name + "'";
        }
        if (phone && (phone !== this.state.modalData.PHONENUMBER)) {
            let set = "w.phonenumber='" + phone + "'";
            update = update ? update + ", " + set : set;
        }
        if (!update) { return (toastr.warning("No changes to update...")); }

        let query = "UPDATE worker w SET " + update + " WHERE w.id=" + id;
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

        let query = "DELETE FROM worker w WHERE w.id=" + id;
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
                toastr.success("Delete success!");
                self.doQuery();
                self.close();
            })
            .catch(function (err) {
                toastr.error("Delete failed...");
                console.error(err);
            });
    }

    render() {
        const button = (<Button bsStyle="success">Add New Worker </Button>);
        const newWorkerForm = <NewWorkerForm />;
        const formBody = (
            <div>
                <form className="form-horizontal" onSubmit={this.doQuery}>
                    <fieldset>
                        <ListGroup>
                            <ListGroupItem>
                                <div className="row">
                                    <div className="form-group col-sm-6">
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Worker
                                            Id</label>
                                        <div className="col-sm-8 col-md-9">
                                            <input type='number' name='wid' ref={ref => this.wid = ref}
                                                placeholder='Worker Id' className="form-control" />
                                        </div>
                                    </div>
                                    <div className="form-group col-sm-6">
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Full
                                            Name</label>
                                        <div className="col-sm-8 col-md-9">
                                            <input type='text' name='wname' ref={ref => this.wname = ref}
                                                placeholder='Full Name' className="form-control" />
                                        </div>
                                    </div>
                                    <div className="form-group col-sm-6">
                                        <label className="control-label text-semibold col-sm-4 col-md-3">Phone
                                            Number</label>
                                        <div className="col-sm-8 col-md-9">
                                            <input type='text' name='name' ref={ref => this.wphone = ref}
                                                placeholder='Phone Number' className="form-control" />
                                        </div>
                                    </div>
                                    <div className="form-group col-sm-6">
                                        <ControlLabel className="col-sm-4 col-md-3">Type</ControlLabel>
                                        <div className="col-sm-8 col-md-9">
                                            <FormControl componentClass="select" inputRef={ref => this.wtype = ref}
                                                placeholder="select">
                                                <option value="worker">ALL</option>
                                                <option value="president">President</option>
                                                <option value="manager">Manager</option>
                                                <option value="employee">Employee</option>
                                            </FormControl>
                                        </div>
                                    </div>
                                </div>
                            </ListGroupItem>
                            <ListGroupItem>
                                <div className="row">
                                    <div className="row">
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
        );
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
                                        <input type='number' disabled name='name' ref={ref => this.peditId = ref} placeholder='ID Number' defaultValue={this.state.modalData.ID} className="form-control" />
                                    </div>
                                </div>
                                <div className="form-group col-sm-6">
                                    <label className="control-label text-semibold col-sm-4 col-md-3">Full Name</label>
                                    <div className="col-sm-8 col-md-9">
                                        <input type='text' name='name' ref={ref => this.peditName = ref} placeholder='Full Name' defaultValue={this.state.modalData.NAME} maxLength={25} className="form-control" />
                                    </div>
                                </div>
                                <div className="form-group col-sm-6">
                                    <label className="control-label text-semibold col-sm-4 col-md-3">Phone Number</label>
                                    <div className="col-sm-8 col-md-9">
                                        <input type='text' name='name' ref={ref => this.peditPhone = ref} placeholder='Phone Number' defaultValue={this.state.modalData.PHONENUMBER} maxLength={13} className="form-control" />
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
                        <ModalOpen eventListener={button} modalBody={newWorkerForm} />
                    </Col>
                </Row>
                <Row>
                    <Col xs={12}>
                        <div className="table-responsive">
                            <JsonTable className="table" rows={this.state.rows} onClickRow={this.onClickRow} />
                        </div>
                    </Col>
                </Row>
            </Grid>
        );
    }
}
