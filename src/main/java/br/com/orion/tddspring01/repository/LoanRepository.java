package br.com.orion.tddspring01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.orion.tddspring01.model.Book;
import br.com.orion.tddspring01.model.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

	@Query("select case when ( count(l.id) > 0 ) then true else false end from Loan l where "+
	  "l.book = book  and ( l.returned is false)")
	boolean isBookAlreadyLoaned(@Param("book") Book book);

}
