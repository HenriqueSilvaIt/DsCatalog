
import './style.css'
export default function PaginationCatalog() {

    return (
        <>

            <div className="dscatalog-pagination-container">
                <button className="pagination-arrow">&lt;</button>
                <button className="pagination-item active">1</button>
                <button className="pagination-item">2</button>
                <button className="pagination-item">3</button>
                <button className="pagination-item">4</button>
                <button className="pagination-item">5</button>
                <button className="pagination-item">6</button>
                <button className="pagination-item">7</button>
                <button className="pagination-item">8</button>
                <button className="pagination-item">9</button>
                <button className="pagination-item">10</button>

                <span className="pagination-ellipsis">...</span>
                <button className="pagination-item">35</button>
                <button className="pagination-arrow">&gt;</button>
            </div>
        </>
    );



}