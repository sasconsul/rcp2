package com.sasconsul.rcp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Words.
 */
@Entity
@Table(name = "words")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Words implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word")
    private String word;

    @Column(name = "count")
    private Long count;

    @Column(name = "page")
    private Long page;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "words_pages",
               joinColumns = @JoinColumn(name="words_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="pages_id", referencedColumnName="id"))
    private Set<Pages> pages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public Words word(String word) {
        this.word = word;
        return this;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Long getCount() {
        return count;
    }

    public Words count(Long count) {
        this.count = count;
        return this;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getPage() {
        return page;
    }

    public Words page(Long page) {
        this.page = page;
        return this;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Set<Pages> getPages() {
        return pages;
    }

    public Words pages(Set<Pages> pages) {
        this.pages = pages;
        return this;
    }

    public Words addPages(Pages pages) {
        this.pages.add(pages);
        return this;
    }

    public Words removePages(Pages pages) {
        this.pages.remove(pages);
        return this;
    }

    public void setPages(Set<Pages> pages) {
        this.pages = pages;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Words words = (Words) o;
        if (words.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), words.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Words{" +
            "id=" + getId() +
            ", word='" + getWord() + "'" +
            ", count='" + getCount() + "'" +
            ", page='" + getPage() + "'" +
            "}";
    }
}
