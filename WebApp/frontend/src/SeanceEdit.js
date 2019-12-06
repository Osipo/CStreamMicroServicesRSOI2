import React, { Component, useContext } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { FormErrors } from './FormErrors';
import './Error.css';
class SeanceEdit extends Component {
    ciname = '';
    constructor(props){
        super(props);
        this.state = {
            cid:'',
            fid: '',
            date:'',
            formErrors: {film: '', date: ''},
            filmValid: false,
            dateValid: false,
            formValid: false
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        Date.prototype.yyyymmdd = function() {
              var mm = this.getMonth() + 1; // getMonth() is zero-based
              var dd = this.getDate();

              return [this.getFullYear(),
                      (mm>9 ? '' : '0') + mm,
                      (dd>9 ? '' : '0') + dd
                     ].join('-');
        };
    }
    async componentDidMount() {
        let id = this.props.match.params.id;
        console.log('Cid = ',id);
        const cinema = await (await fetch('/v1/api/cinemas/'+id).then(response => response.json()));
        this.ciname = cinema.name;
        this.setState({
                cid: cinema.name,
                fid: '',
                date: new Date().yyyymmdd(),
                formErrors: {cinema: '', film: '', date: ''},
                cinemaValid: true,
                filmValid: true,
                dateValid: true,
                formValid: false
        }, () => {for(let p in this.state){ this.validateField(p,this.state[p]);} });
        
    }
    
    async handleSubmit(event) {
        event.preventDefault();//TODO: VALIDATE AGAIN
         for(let p in this.state){
            this.validateField(p,this.state[p]);
         }
         if(!this.state.formValid){
            alert('Validation failed!');
            return -1;
        }
        
        
        let s = {cid: this.props.match.params.id, fid: this.state.fid, date: this.state.date};
        
        if(!this.state.formValid){
            alert('Validation failed');
            return -1;
        }
        const film = await (await fetch('/v1/api/films/name/'+this.state.fid).then(response => response.json()));
        console.log(film);
        if(film.length === 1){
            console.log(this.props.location.state);
            for(let p in this.props.location.state){
                console.log(" ",p,this.props.location.state[p]);
            }
            s = {cid: Number(this.props.match.params.id), fid: film[0].id, date: this.state.date};
            const {ns} = this.props.location.state;
            ns.seances.push(s);
            this.props.history.push('/views/cinemas/'+this.props.match.params.id, {ns: ns});
        }
        else{
            alert(film.reason);
            return -1;
        }
    }
    
    validateField(fieldName, value) {
        const item = this.state;
        let fieldValidationErrors = item.formErrors;
        let cinemaValid = item.cinemaValid;
        let filmValid = item.filmValid;
        let dateValid = item.dateValid;
        switch(fieldName) {
          /*case 'cid':
            cinemaValid = value.match(/^[A-Za-z][A-Za-z0-9\s]+$/);
            fieldValidationErrors.cinema = cinemaValid ? '' : ' is invalid. Require words!';
            cinemaValid = cinemaValid && value.length > 3;
            fieldValidationErrors.cinema = cinemaValid ? '' : fieldValidationErrors.cinema+' Length must be more than 3 characters!';
            break;*/
          case 'fid':
            filmValid =  value.match(/^[A-Za-z\s]+$/);
            fieldValidationErrors.film = filmValid ? '': ' is invalid. Require words!';
            break;
          case 'date':
            dateValid = (value !== null && value.match(/^[0-9]{4}-[0-9]{2}-[0-9]{2}$/));
            fieldValidationErrors.date = dateValid ? '' : ' is invalid. Format: yyyy-mm-dd!';
            break;
          default:
            break;
        }
        this.setState({formErrors: fieldValidationErrors,
                        cinemaValid: cinemaValid,
                        filmValid: filmValid,
                        dateValid: dateValid
                      }, () => this.validateForm());
    }
    
    validateForm() {
          this.setState({formValid: this.state.cinemaValid && this.state.filmValid && this.state.dateValid}, () => this.moveToP());
          console.log(this.state.formValid);
    }
    
    moveToP(){
          
          let p = document.getElementsByClassName('has-error');
          console.log(p.length);
          if(p.length !== 0){
              Array.prototype.forEach.call(p, function(el,idx,arr){
                  //console.log(el.tagName);
                  if(el.tagName === 'P'){
                      let parid = el.textContent.split(' ')[0];
                      console.log('Id = ',parid);
                      parid = parid === 'cinema' ? 'cid' : (parid === 'film') ? 'fid' : 'date';
                      let par = document.getElementById(parid).parentNode;
                      par.appendChild(el);
                  }
              },this);
          }
          return 1;
   }
   
   handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        this.setState({[name]: value},
                      () => { this.validateField(name, value) });
   }
    
    
    
    errorClass(error) {
        return(error.length === 0 ? '' : ' has-error');
   }
   
   render(){
        const item = this.state;
        const title = <h2>{'Add Seance'}</h2>;
        const cname = this.ciname ? <h5>{'To cinema   '+this.ciname}</h5> : null;
        return <div>
      <AppNavbar meid={3}/>
      <Container>
        {title}
        {cname}
        <Form onSubmit={this.handleSubmit}>
          <div className="panel panel-default">
            <FormErrors formErrors={item.formErrors} />
          </div>
          <FormGroup className={'form-group'+this.errorClass(item.formErrors.film)}>
            <Label htmlFor="fid">Film</Label>
            <Input type="text" name="fid" id="fid" className="form-control" value={item.fid || ''}
                   onChange={this.handleChange} autoComplete="fid"/>
          </FormGroup>
          <FormGroup className={'form-group'+this.errorClass(item.formErrors.date)}>
            <Label htmlFor="date">Date</Label>
            <Input type="text" name="date" id="date" className="form-control" value={item.date || ''}
                   onChange={this.handleChange} autoComplete="date"/>
          </FormGroup>
          <FormGroup>
            <Button color="primary" type="submit" className="btn btn-primary" disabled={!item.formValid}>Save</Button>{' '}
            <Button color="secondary" tag={Link} to={"/views/cinemas/"+this.props.match.params.id}>Cancel</Button>
          </FormGroup>
        </Form>
      </Container>
    </div>
        
    }
}
export default withRouter(SeanceEdit);