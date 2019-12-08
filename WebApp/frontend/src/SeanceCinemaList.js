import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import $ from 'jquery';
class SeanceCinemaList extends Component {

  constructor(props) {
    super(props);
    this.state = {cs: [], isLoading: true,
              currentPage: 1,
              pageSize: 2,
              upperPageBound: 3,
              lowerPageBound: 0,
              isPrevBtnActive: 'disabled',
              isNextBtnActive: '',
              pageBound: 3
    };
            this.handleClick = this.handleClick.bind(this);
            this.btnDecrementClick = this.btnDecrementClick.bind(this);
            this.btnIncrementClick = this.btnIncrementClick.bind(this);
            this.btnNextClick = this.btnNextClick.bind(this);
            this.btnPrevClick = this.btnPrevClick.bind(this);
            this.componentDidMount = this.componentDidMount.bind(this);
            this.setPrevAndNextBtnClass = this.setPrevAndNextBtnClass.bind(this);
  }
  
  componentDidMount() {
    this.setState({isLoading: true});
    let id = this.props.match.params.id;
    console.log(id);
    fetch('/v1/api/cinemas/'+id+'/seances')
      .then(response => response.json())
      .then(data => this.setState({cs: data, isLoading: false}));
  }
  
  componentDidUpdate() {
                $("ul li.active").removeClass('active');
                $('ul li#'+this.state.currentPage).addClass('active');
          }
          handleClick(event) {
            let listid = Number(event.target.id);
            this.setState({
              currentPage: listid
            });
            $("ul li.active").removeClass('active');
            $('ul li#'+listid).addClass('active');
            this.setPrevAndNextBtnClass(listid);
          }
          setPrevAndNextBtnClass(listid) {
            let totalPage = Math.ceil(this.state.cs.length / this.state.pageSize);
            this.setState({isNextBtnActive: 'disabled'});
            this.setState({isPrevBtnActive: 'disabled'});
            if(totalPage === listid && totalPage > 1){
                this.setState({isPrevBtnActive: ''});
            }
            else if(listid === 1 && totalPage > 1){
                this.setState({isNextBtnActive: ''});
            }
            else if(totalPage > 1){
                this.setState({isNextBtnActive: ''});
                this.setState({isPrevBtnActive: ''});
            }
        }
          btnIncrementClick() {
              this.setState({upperPageBound: this.state.upperPageBound + this.state.pageBound});
              this.setState({lowerPageBound: this.state.lowerPageBound + this.state.pageBound});
              let listid = this.state.upperPageBound + 1;
              this.setState({ currentPage: listid});
              this.setPrevAndNextBtnClass(listid);
        }
          btnDecrementClick() {
            this.setState({upperPageBound: this.state.upperPageBound - this.state.pageBound});
            this.setState({lowerPageBound: this.state.lowerPageBound - this.state.pageBound});
            let listid = this.state.upperPageBound - this.state.pageBound;
            this.setState({ currentPage: listid});
            this.setPrevAndNextBtnClass(listid);
        }
        btnPrevClick() {
            if((this.state.currentPage -1)%this.state.pageBound === 0 ){
                this.setState({upperPageBound: this.state.upperPageBound - this.state.pageBound});
                this.setState({lowerPageBound: this.state.lowerPageBound - this.state.pageBound});
            }
            let listid = this.state.currentPage - 1;
            this.setState({ currentPage : listid});
            this.setPrevAndNextBtnClass(listid);
        }
        btnNextClick() {
            if((this.state.currentPage +1) > this.state.upperPageBound ){
                this.setState({upperPageBound: this.state.upperPageBound + this.state.pageBound});
                this.setState({lowerPageBound: this.state.lowerPageBound + this.state.pageBound});
            }
            let listid = this.state.currentPage + 1;
            this.setState({ currentPage : listid});
            this.setPrevAndNextBtnClass(listid);
        }
  
  render() {

    const { cs,isLoading, currentPage, pageSize,upperPageBound,lowerPageBound,isPrevBtnActive,isNextBtnActive } = this.state;
            
             if (isLoading) {
                return <p>Loading...</p>;
            }
            let seances = cs.seances;
            let ci = cs.cinema;
            // Logic for displaying current todos
            const indexOfLastTodo = currentPage * pageSize;
            const indexOfFirstTodo = indexOfLastTodo - pageSize;
            const currentTodos = seances.slice(indexOfFirstTodo, indexOfLastTodo);
            const renderSeances = currentTodos.map((s, index) => {
              return <tr key={index}>
                 <td style={{whiteSpace: 'nowrap'}}>{ci.name}</td>
                  <td>{s.fid}</td>
                  <td>{s.date}</td>
                  <td>
                      <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/views/seances/"+s.cid+"/"+s.fid}>Info</Button>
                      </ButtonGroup>
                  </td>
              </tr>;
            });

            // Logic for displaying page numbers
            const pageNumbers = [];
            for (let i = 1; i <= Math.ceil(seances.length / pageSize); i++) {
              pageNumbers.push(i);
            }

            const renderPageNumbers = pageNumbers.map(number => {
                if(number === 1 && currentPage === 1){
                    return(
                        <li key={number} className='active' id={number}><a href='#' id={number} onClick={this.handleClick}>{number}</a></li>
                    )
                }
                else if((number < upperPageBound + 1) && number > lowerPageBound){
                    return(
                        <li key={number} id={number}><a href='#' id={number} onClick={this.handleClick}>{number}</a></li>
                    )
                }
            });
            let pageIncrementBtn = null;
            if(pageNumbers.length > upperPageBound){
                pageIncrementBtn = <li className=''><a href='#' onClick={this.btnIncrementClick}> &hellip; </a></li>
            }
            let pageDecrementBtn = null;
            if(lowerPageBound >= 1){
                pageDecrementBtn = <li className=''><a href='#' onClick={this.btnDecrementClick}> &hellip; </a></li>
            }
            let renderPrevBtn = null;
            if(isPrevBtnActive === 'disabled') {
                renderPrevBtn = <li className={isPrevBtnActive}><span id="btnPrev"> Prev </span></li>
            }
            else{
                renderPrevBtn = <li className={isPrevBtnActive}><a href='#' id="btnPrev" onClick={this.btnPrevClick}> Prev </a></li>
            }
            let renderNextBtn = null;
            if(isNextBtnActive === 'disabled') {
                renderNextBtn = <li className={isNextBtnActive}><span id="btnNext"> Next </span></li>
            }
            else{
                renderNextBtn = <li className={isNextBtnActive}><a href='#' id="btnNext" onClick={this.btnNextClick}> Next </a></li>
            }
  
    /*const sList = s.map(s => {
      return <tr key={s.cid+" "+s.fid}>
        <td style={{whiteSpace: 'nowrap'}}>{ci.name}</td>
        <td>{s.fid}</td>
        <td>{s.date}</td>
        <td>
          <ButtonGroup>
            <Button size="sm" color="primary" tag={Link} to={"/views/seances/"+s.cid+"/"+s.fid}>Info</Button>
          </ButtonGroup>
        </td>
      </tr>
    });*/
    
    
    return (
      <div>
        <AppNavbar meid={3}/>
        <Container fluid>
          <div className="float-right">
            <Button color="danger" tag={Link} to="/views/cinemas">Back</Button>
          </div>
          <h3>Seances in {ci.Name}</h3>
          <Table className="mt-4">
            <thead>
            <tr>
              <th width="20%">Cinema</th>
              <th width="10%">FilmId</th>
              <th width="20%">Date</th>
              <th width="10%">Actions</th>
            </tr>
            </thead>
            <tbody>
            {renderSeances}
            </tbody>
          </Table>
          <ul id="page-numbers" className="pagination">
                  {renderPrevBtn}
                  {pageDecrementBtn}
                  {renderPageNumbers}
                  {pageIncrementBtn}
                  {renderNextBtn}
          </ul>
        </Container>
      </div>
    );
  }
  
}
export default SeanceCinemaList;