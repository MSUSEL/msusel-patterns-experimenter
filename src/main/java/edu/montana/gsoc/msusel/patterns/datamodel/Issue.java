package edu.montana.gsoc.msusel.patterns.datamodel;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder(buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Issue {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @XStreamOmitField
    private long identifier;

    @Getter
    @Column
    private String key;
    @Getter
    @Column
    private int start;
    @Getter
    @Column
    private int end;
}
