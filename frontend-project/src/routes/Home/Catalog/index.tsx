
import CatalogCard from '../../../components/CatalogCard';
import PaginationCatalog from '../../../components/PaginationCatalog';
import SearchCatalogBar from '../../../components/SearchCatalogBar';
import './style.css';

export default function Catalog() {


    return (
        <>
            <main >
                <section className="dscatalog-catalog-container">

                    <SearchCatalogBar />

                    <CatalogCard />


                    <PaginationCatalog/>
                </section>
            </main>
        </>
    );


}